package com.fastcampus.hellospringbatch.job;

import com.fastcampus.hellospringbatch.BatchTestConfig;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;


@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {HelloJobConfig.class, BatchTestConfig.class})
class HelloJobConfigTest {

    private final JobLauncherTestUtils jobLauncherTestUtils;

    public HelloJobConfigTest(@Autowired JobLauncherTestUtils jobLauncherTestUtils) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
    }

    @Test
    void helloJob() throws Exception {
        // Given
        JobExecution execution = jobLauncherTestUtils.launchJob();

        // Then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
    }

}