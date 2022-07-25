package com.fastcampus.hellospringbatch.job.player;

import com.fastcampus.hellospringbatch.core.service.PlayerSalaryService;
import com.fastcampus.hellospringbatch.dto.PlayerDto;
import com.fastcampus.hellospringbatch.dto.PlayerSalaryDto;
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
import org.springframework.batch.item.adapter.ItemProcessorAdapter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.batch.item.file.builder.FlatFileItemWriterBuilder;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

import java.io.File;
import java.io.IOException;

@Slf4j
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
            FlatFileItemWriter<PlayerSalaryDto> flatFileItemWriter
    ) {
        return stepBuilderFactory.get("flatFileStep")
                .<PlayerDto, PlayerSalaryDto>chunk(5)
                .reader(playerFileItemReader)
                .processor(playerSalaryProcessorAdapter)
                .writer(flatFileItemWriter)
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
    public FlatFileItemWriter<PlayerSalaryDto> flatFileItemWriter() throws IOException {
        log.error("[flatFileItemWriter] start flatFileItemWriter");

        //클래스를 기반으로 FieldExtractor 만들기 때문에 BeanWrapperFieldExtractor 생성
        BeanWrapperFieldExtractor<PlayerSalaryDto> fieldExtractor = new BeanWrapperFieldExtractor<>();

        // PlayerSalaryDto 멤버 변수 이름을 나열한다.
        fieldExtractor.setNames(new String[]{"ID", "firstName", "lastName", "salary"});
        fieldExtractor.afterPropertiesSet();

        DelimitedLineAggregator<PlayerSalaryDto> lineAggregator = new DelimitedLineAggregator<>();
        lineAggregator.setDelimiter("\t"); //델리미터를 TAP으로 지정
        lineAggregator.setFieldExtractor(fieldExtractor);

        // 기존에 파일을 덮어쓴다.
        new File("player-salary-list.txt").createNewFile();
        FileSystemResource resource = new FileSystemResource("player-salary-list.txt");

        return new FlatFileItemWriterBuilder<PlayerSalaryDto>()
                .name("flatFileItemWriter")
                .resource(resource) //데이터를 입력할 리소스 지정
                .lineAggregator(lineAggregator) //어떤 조합으로 파일에 입력할 건지
                .build();
    }

}
