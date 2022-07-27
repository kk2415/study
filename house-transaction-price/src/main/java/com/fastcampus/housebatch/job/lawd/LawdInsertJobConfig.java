package com.fastcampus.housebatch.job.lawd;

import com.fastcampus.housebatch.core.entity.Lawd;
import com.fastcampus.housebatch.core.service.LawdService;
import com.fastcampus.housebatch.job.validator.FilePathParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import static com.fastcampus.housebatch.job.lawd.LawdFieldSetMapper.*;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class LawdInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final LawdService lawdService;

    @Bean
    public Job lawdInsertJob(Step lawdInsertStep) {
        return jobBuilderFactory.get("lawdInsertJob")
                .validator(new FilePathParameterValidator())
                .incrementer(new RunIdIncrementer())
                .start(lawdInsertStep)
                .build();
    }

    @Bean
    @JobScope
    public Step lawdInsertStep(
            FlatFileItemReader<Lawd> lawdFileItemReader,
            ItemWriter<Lawd> lawdItemWriter
    ) {
        return stepBuilderFactory.get("lawdInsertStep")
                .<Lawd, Lawd>chunk(1_000)
                .reader(lawdFileItemReader)
                .writer(lawdItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<Lawd> lawdFileItemReader(
            @Value("#{jobParameters['filePath']}") String filePath
    ) {
        return new FlatFileItemReaderBuilder<Lawd>()
                .name("lawdFileItemReader")
                .delimited()
                .delimiter("\t")
                .names(LAWD_CD, LAWD_DONG, EXIST)
                .linesToSkip(1)
                .fieldSetMapper(new LawdFieldSetMapper())
                .resource(new ClassPathResource(filePath))
                .build();
    }

    @Bean
    @StepScope
    public ItemWriter<Lawd> lawdItemWriter() {
        return items -> {
            items.forEach(
                    item -> {
                        System.out.println(item);
                        lawdService.upsert(item);
                    }
            );
        };
    }

}
