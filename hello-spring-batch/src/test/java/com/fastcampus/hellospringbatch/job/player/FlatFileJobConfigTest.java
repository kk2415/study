package com.fastcampus.hellospringbatch.job.player;

import com.fastcampus.hellospringbatch.BatchTestConfig;
import com.fastcampus.hellospringbatch.core.service.PlayerSalaryService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.AssertFile;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@SpringBatchTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("text")
@ContextConfiguration(classes = {FlatFileJobConfig.class, BatchTestConfig.class, PlayerSalaryService.class})
class FlatFileJobConfigTest {

    private JobLauncherTestUtils jobLauncherTestUtils;

    public FlatFileJobConfigTest(@Autowired JobLauncherTestUtils jobLauncherTestUtils) {
        this.jobLauncherTestUtils = jobLauncherTestUtils;
    }

    @Test
    void success() throws Exception {
        // Given

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        
        AssertFile.assertFileEquals(new FileSystemResource("player-salary-list.txt"),
                new FileSystemResource("succeed-player-salary-list.txt"));
    }

}