package com.fastcampus.hellospringbatch.job.player;

import com.fastcampus.hellospringbatch.core.service.PlayerSalaryService;
import com.fastcampus.hellospringbatch.dto.PlayerDto;
import com.fastcampus.hellospringbatch.dto.PlayerSalaryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

@Configuration
@RequiredArgsConstructor
public class FlatFileJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job flatFileJob(Step flatFileStep) {
        return jobBuilderFactory.get("flatFileJob")
                .incrementer(new RunIdIncrementer())
                .start(flatFileStep)
                .build();
    }

    @Bean
    @JobScope
    public Step flatFileStep(
            FlatFileItemReader<PlayerDto> playerFileItemReader,
            ItemProcessor<PlayerDto, PlayerSalaryDto> playerItemProcessor,
            ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> playerSalaryProcessorAdapter,
            ItemWriter<PlayerSalaryDto> playerFileItemWriter
    ) {
        return stepBuilderFactory.get("flatFileStep")
                .<PlayerDto, PlayerSalaryDto>chunk(5)
                .reader(playerFileItemReader)
                .processor(playerSalaryProcessorAdapter)
                .writer(playerFileItemWriter)
                .build();
    }

    @Bean
    @StepScope
    public FlatFileItemReader<PlayerDto> playerFileItemReader() {
        return new FlatFileItemReaderBuilder<PlayerDto>()
                .name("playerFileItemReader")
                .lineTokenizer(new DelimitedLineTokenizer())
                .linesToSkip(1)
                .fieldSetMapper(new PlayerFieldSetMapper())
                .resource(new FileSystemResource("player-list.txt"))
                .build();
    }

    @Bean
    @StepScope
    public ItemProcessor<PlayerDto, PlayerSalaryDto> playerItemProcessor(PlayerSalaryService playerSalaryService) {
        return playerSalaryService::calSalary;
    }

    /**
     * 로직을 처리하는 서비스 클래스가 있다면
     * Adapter 를 사용하면 쉽게 서비스를 재사용할 수 있다.
     * */
    @Bean
    @StepScope
    public ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> playerSalaryProcessorAdapter(PlayerSalaryService playerSalaryService) {
        ItemProcessorAdapter<PlayerDto, PlayerSalaryDto> adapter = new ItemProcessorAdapter<>();
        adapter.setTargetObject(playerSalaryService);
        adapter.setTargetMethod("calSalary");

        return adapter;
    }

    @Bean
    @StepScope
    public ItemWriter<PlayerSalaryDto> playerFileItemWriter() {
        return items -> {
            items.forEach(System.out::println);
        };
    }

}
