package com.fastcampus.hellospringbatch.job;

import com.fastcampus.hellospringbatch.job.validator.LocalDateParameterValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.LocalDate;

@Slf4j
@Configuration
@RequiredArgsConstructor
public class AdvancedJobConfig {

    private final JobBuilderFactory jobBuilderFactory;
    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job advancedJob(
            Step advancedStep,
            LocalDateParameterValidator validator,
            JobExecutionListener jobExecutionListener
    ) {
        return jobBuilderFactory
                .get("advancedJob")
                .validator(validator)
                .listener(jobExecutionListener)
                .incrementer(new RunIdIncrementer())
                .start(advancedStep)
                .build();
    }

    @Bean
    @JobScope
    public JobExecutionListener jobExecutionListener() {
        return new JobExecutionListener() {
            @Override
            public void beforeJob(JobExecution jobExecution) {
                log.info("한글");
                log.info("[JobExecutionListener] beforeJob jobExecution status = {}", jobExecution.getStatus());
            }

            @Override
            public void afterJob(JobExecution jobExecution) {
                if (jobExecution.getStatus() == BatchStatus.FAILED) {
                    //fail 시 이메일로 알려주는 등 로직을 추가할 수도 있다.
                    log.error("[JobExecutionListener] afterJob jobExecution status is FAILED!!!");
                }
                else {
                    log.info("[JobExecutionListener] afterJob jobExecution status = {}", jobExecution.getStatus());
                }
            }
        };
    }

    @Bean
    @JobScope
    public Step advancedStep(
            Tasklet advancedTasklet,
            StepExecutionListener stepExecutionListener
    ) {
        return stepBuilderFactory.get("advancedStep")
                .listener(stepExecutionListener)
                .tasklet(advancedTasklet)
                .build();
    }

    /**
     * 보통은 스텝 리스너를 잘 사용은 안한다.
     * 스텝 전후의 어떤 처리를 할 것이 아니라면 굳이 사용하지 않아도 된다.
     * */
    @Bean
    @StepScope
    public StepExecutionListener stepExecutionListener() {
        return new StepExecutionListener() {
            @Override
            public void beforeStep(StepExecution stepExecution) {
                log.info("[StepExecutionListener] beforeJob StepExecution status = {}", stepExecution.getStatus());
            }

            @Override
            public ExitStatus afterStep(StepExecution stepExecution) {
                log.info("[StepExecutionListener] afterStep StepExecution status = {}", stepExecution.getStatus());
                return stepExecution.getExitStatus();
            }
        };
    }

    @Bean
    @StepScope
    public Tasklet advancedTasklet(@Value("#{jobParameters['targetDate']}") String targetDate) {
        return ((contribution, chunkContext) -> {
            LocalDate date = LocalDate.parse(targetDate);

            log.info("[advancedTasklet] jobParameters = {}", targetDate);
            log.info("[advancedTasklet] executed advanced Tasklet");

            return RepeatStatus.FINISHED;
        });
    }

}
