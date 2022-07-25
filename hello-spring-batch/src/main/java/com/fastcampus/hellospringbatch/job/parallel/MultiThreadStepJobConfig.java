package com.fastcampus.hellospringbatch.job.parallel;

import com.fastcampus.hellospringbatch.dto.AmountDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;

import java.io.File;
import java.io.IOException;

/**
 * 단일프로세스 환경에서 청크 단위로 병렬 처리한다.
 * 하나의 쓰레드가 하나의 청크를 실행한다.
 * */
@Configuration
@RequiredArgsConstructor
public class MultiThreadStepJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job multiThreadStepJob(Step multiThreadStep) {
        return jobBuilderFactory.get("multiThreadStepJob")
                .incrementer(new RunIdIncrementer())
                .start(multiThreadStep)
                .build();
    }

    @Bean
    @JobScope
    public Step multiThreadStep(
            FlatFileItemReader<AmountDto> amountFileItemReader,
            ItemProcessor<AmountDto, AmountDto> amountItemProcessor,
            FlatFileItemWriter<AmountDto> amountFileItemWriter,
            TaskExecutor taskExecutor
    ) {
        return stepBuilderFactory.get("multiThreadStep")
                .<AmountDto, AmountDto>chunk(10)
                .reader(amountFileItemReader)
                .processor(amountItemProcessor)
                .writer(amountFileItemWriter)
                .taskExecutor(taskExecutor)
                .build();
    }

    /**
     * 멀티쓰레드로 병렬 처리할 때는 TaskExecutor 를 사용한다.
     * 하나의 쓰레드가 하나의 청크를 처리하는 방식
     * */
    @Bean
    public TaskExecutor taskExecutor() {
        SimpleAsyncTaskExecutor taskExecutor = new SimpleAsyncTaskExecutor("spring-batch-task-executor");
        taskExecutor.setConcurrencyLimit(4); //동시에 실행되는 쓰레드를 4개로 지정
        return taskExecutor;
    }

    @Bean
    @StepScope
    public FlatFileItemReader<AmountDto> amountFileItemReader() {
        return new FlatFileItemReaderBuilder<AmountDto>()
                .name("amountFileItemReader")
                .lineTokenizer(new DelimitedLineTokenizer(DelimitedLineTokenizer.DELIMITER_TAB))
                .resource(new FileSystemResource("data/input.txt"))
                .fieldSetMapper(new AmountFieldSetMapper())
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<AmountDto, AmountDto> amountItemProcessor() {
        return item -> {
            System.out.println(item + "\tCurrent Thread = " + Thread.currentThread().getName());
            item.setAmount(item.getAmount() * 100);
            return item;
        };
    }

    @Bean
    @StepScope
    public FlatFileItemWriter<AmountDto> amountFileItemWriter() throws IOException {
        BeanWrapperFieldExtractor<AmountDto> fieldExtractor = new BeanWrapperFieldExtractor<>();
        fieldExtractor.setNames(new String[]{"index", "name", "amount"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<AmountDto> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setFieldExtractor(fieldExtractor);

        String filePath = "data/output.txt";
        new File(filePath).createNewFile();

        return new FlatFileItemWriterBuilder<AmountDto>()
                .name("amountFileItemWriter")
                .resource(new FileSystemResource(filePath))
                .lineAggregator(lineAggregator)
                .build();
    }

}
