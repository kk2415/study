package com.fastcampus.housebatch.job.notify;

import com.fastcampus.housebatch.adapter.FakeSendService;
import com.fastcampus.housebatch.core.dto.AptDto;
import com.fastcampus.housebatch.core.dto.NotificationDto;
import com.fastcampus.housebatch.core.entity.AptNotification;
import com.fastcampus.housebatch.core.repository.AptNotificationRepository;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import com.fastcampus.housebatch.job.validator.DealDateParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.data.RepositoryItemReader;
import org.springframework.batch.item.data.builder.RepositoryItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Sort;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AptNotificationJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job aptNotificationJob(Step aptNotificationStep) {
        return jobBuilderFactory.get("aptNotificationJob")
                .validator(new DealDateParameterValidator())
                .incrementer(new RunIdIncrementer())
                .start(aptNotificationStep)
                .build();
    }

    @Bean
    @JobScope
    public Step aptNotificationStep(
            RepositoryItemReader<AptNotification> aptNotificationItemReader,
            ItemProcessor<AptNotification, NotificationDto> aptNotificationItemProcessor,
            ItemWriter<NotificationDto> aptNotificationItemWriter
    ) {
        return stepBuilderFactory.get("aptNotificationStep")
                .<AptNotification, NotificationDto>chunk(10)
                .reader(aptNotificationItemReader)
                .processor(aptNotificationItemProcessor)
                .writer(aptNotificationItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public RepositoryItemReader<AptNotification> aptNotificationItemReader(
            AptNotificationRepository aptNotificationRepository
    ) {
        return new RepositoryItemReaderBuilder<AptNotification>()
                .name("aptNotificationItemReader")
                .repository(aptNotificationRepository)
                .methodName("findByEnabledIsTrue")
                .pageSize(10)
                .arguments(List.of())
                .sorts(Collections.singletonMap("aptNotificationId", Sort.Direction.DESC))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<AptNotification, NotificationDto> aptNotificationItemProcessor(
            @Value("#{jobParameters['dealDate']}") String dealDate,
            LawdRepository lawdRepository,
            AptDealService aptDealService
    ) {
        return aptNotification -> {
            List<AptDto> aptDtoList = aptDealService.findByGuLawdCdAndDealDate(
                    aptNotification.getGuLawdCd(), LocalDate.parse(dealDate));

            if (aptDtoList.isEmpty()) {
                return null;
            }

            String guName = lawdRepository.findByLawdCd(aptNotification.getGuLawdCd() + "00000")
                    .orElseThrow().getLawdDong();

            return NotificationDto.builder()
                    .email(aptNotification.getEmail())
                    .guName(guName)
                    .count(aptDtoList.size())
                    .aptDtoList(aptDtoList)
                    .build();
        };
    }

    @Bean
    @StepScope
    public ItemWriter<NotificationDto> aptNotificationItemWriter(FakeSendService sendService) {
        return items -> {
            items.forEach(item -> sendService.send(item.getEmail(), item.toMessage()));
        };
    }
}
