package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.dto.AptDealDto;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
import com.fastcampus.housebatch.job.validator.FilePathParameterValidator;
import com.fastcampus.housebatch.job.validator.LawdCdParameterValidator;
import com.fastcampus.housebatch.job.validator.YearMonthParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.CompositeJobParametersValidator;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.builder.StaxEventItemReaderBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.config.Task;

import java.time.YearMonth;
import java.util.Arrays;
import java.util.List;


/**
 * 특정 년월에 발생한 특정 구의 아파트 거래정보를 알려주는 job
 * */
@Slf4j
@Configuration
@RequiredArgsConstructor
public class AptDealInsertJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;
    private final ApartmentApiResource apartmentApiResource;
    private final AptDealService aptDealService;

    @Bean
    public Job aptDealInsertJob(
            Step aptDealInsertStep,
            Step guLawdCdStep
    ) {
        return jobBuilderFactory.get("aptDealInsertJob")
                .incrementer(new RunIdIncrementer())
                .validator(aptDealJobParameterValidator())

                // guLawdCdStep리턴값 상태가 CONTINUABLE이면 stepContextPrintStep 실행 후 다시 guLawdCdStep 실행
                .start(guLawdCdStep)
                .on("CONTINUABLE").to(aptDealInsertStep).next(guLawdCdStep)

                // guLawdCdStep 실행했는데 상태가 CONTINUABLE이 아니면 종료
                .from(guLawdCdStep)
                .on("*").end()
                .end()
                .build();
    }

    /**
     * Validator를 2개 이상 사용할 때는 CompositeJobParametersValidator를 이용한다.
     * */
    private JobParametersValidator aptDealJobParameterValidator() {
        CompositeJobParametersValidator compositeValidator = new CompositeJobParametersValidator();

        //List에 등록한 순서대로 validator가 수행됨
        compositeValidator.setValidators(List.of(new YearMonthParameterValidator()));

        return compositeValidator;
    }

    /**
     * ExecutionContext를 이용해서 Step 간에 데이터 이동
     * guLawdCdStep(구 코드 전달) -> aptDealInsertStep
     * */
    @Bean
    @JobScope
    public Step guLawdCdStep(Tasklet guLawdCdTaskLet) {
        return stepBuilderFactory.get("guLawdCdStep")
                .tasklet(guLawdCdTaskLet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet guLawdCdTaskLet(LawdRepository lawdRepository) {
        return new GuLawdTasklet(lawdRepository);
    }

    @Bean
    @JobScope
    public Step aptDealInsertStep(
            StaxEventItemReader<AptDealDto> aptDealResourceReader,
            ItemWriter<AptDealDto> aptDealDtoItemWriter
    ) {
        return stepBuilderFactory.get("aptDealInsertStep")
                .<AptDealDto, AptDealDto>chunk(10)
                .reader(aptDealResourceReader)
                .writer(aptDealDtoItemWriter)
                .build();
    }

    /**
     * xml 파일 reader 설정
     * */
    @Bean
    @StepScope
    public StaxEventItemReader<AptDealDto> aptDealResourceReader(
            @Value("#{jobParameters['yearMonth']}") String yearMonth,
            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd,
            Jaxb2Marshaller aptDealDtoMarshaller
    ) {
        return new StaxEventItemReaderBuilder<AptDealDto>()
                .name("aptDealResourceReader")
                .resource(apartmentApiResource.getResource(guLawdCd, YearMonth.parse(yearMonth)))
                .addFragmentRootElements("item") //xml 파일에서 읽을 루트 element 지정
                .unmarshaller(aptDealDtoMarshaller) //xml 파일을 객체에 매핑시켜줌
                .build();
    }

    /**
     * xml 파일과 DTO 객체 매핑
     * */
    @Bean
    @StepScope
    public Jaxb2Marshaller aptDealDtoMarshaller() {
        Jaxb2Marshaller jaxb2Marshaller = new Jaxb2Marshaller();
        jaxb2Marshaller.setClassesToBeBound(AptDealDto.class);

        return jaxb2Marshaller;
    }

    @Bean
    @StepScope
    public ItemWriter<AptDealDto> aptDealDtoItemWriter() {
        return items -> {
            items.forEach(aptDealService::upsert);
        };
    }



    @Bean
    @JobScope
    // 주의할 점 : Step 파라미터에 @Value() 애노테이션으로 데이터를 가져오면
    // Step 처음 실행되고 그 값이 끝날 때 까지 유지된다.
    // Step이 실행될 때 마다 유동적으로 데이터가 바뀌길 원한다면 @Value() 애노테이션은
    // Tasklet이나 Reader에 작성해야한다.
    public Step stepContextPrintStep(Tasklet contextPrintTasklet) {
        return stepBuilderFactory.get("stepContextPrintStep")
                .tasklet(contextPrintTasklet)
                .build();
    }

    @Bean
    @StepScope
    public Tasklet contextPrintTasklet(
            @Value("#{jobExecutionContext['guLawdCd']}") String guLawdCd
    ) {
        return (contribution, chunkContext) -> {
            System.out.println(guLawdCd);
            return RepeatStatus.FINISHED;
        };
    }
}
