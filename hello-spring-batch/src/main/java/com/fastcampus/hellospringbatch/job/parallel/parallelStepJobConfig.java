package com.fastcampus.hellospringbatch.job.parallel;

import com.fastcampus.hellospringbatch.dto.AmountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.job.flow.support.SimpleFlow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
* 단일 프로세스 멀티 쓰레드에서 Flow룰 사용해 스텝을 동시에 실행한다.
**/
@Configuration
@RequiredArgsConstructor
public class parallelStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    /**
     * Job에 Step이 아닌 Flow를 추가한다.
     * */
    @Bean
    public Job parallelJob(Flow splitFlow) {
        return jobBuilderFactory.get("parallelJob")
                .incrementer(new RunIdIncrementer())
                .start(splitFlow)
                .build()
                .build();
    }

    /**
     * 여러 Flow들을 조합할 기본 Flow를 지정한다.
     * */
    @Bean
    public Flow splitFlow(
            TaskExecutor taskExecutor,
            Flow flowAmountFileStep,
            Flow flowAnotherStep
    ) {
        return new FlowBuilder<SimpleFlow>("splitFlow")
                .split(taskExecutor)
                .add(flowAmountFileStep, flowAnotherStep) //실행할 flow를 지정
                .build();

    }

    /**
     * Flow마다 실행할 Step을 지정해준다.
     * */
    @Bean
    public Flow flowAmountFileStep(Step amountFileStep) {
        return new FlowBuilder<SimpleFlow>("flowAmountFileStep")
                .start(amountFileStep)
                .build();
    }

    @Bean
    public Step amountFileStep(
            FlatFileItemReader<AmountDto> amountFileItemReader,
            ItemProcessor<AmountDto, AmountDto> amountItemProcessor,
            FlatFileItemWriter<AmountDto> amountFileItemWriter
    ) {
        return stepBuilderFactory.get("amountFileStep")
                .<AmountDto, AmountDto>chunk(10)
                .reader(amountFileItemReader)
                .processor(amountItemProcessor)
                .writer(amountFileItemWriter)
                .build();
    }

    @Bean
    public Flow flowAnotherStep(Step anotherStep) {
        return new FlowBuilder<SimpleFlow>("flowAnotherStep")
                .start(anotherStep)
                .build();
    }

    @Bean
    public Step anotherStep() {
        return stepBuilderFactory.get("anotherStep")
                .tasklet((contribution, chunkContext) -> {
                    Thread.sleep(500);
                    System.out.println("Another Step Completed. Thread = " + Thread.currentThread().getName());
                    return RepeatStatus.FINISHED;
                })
                .build();
    }

}