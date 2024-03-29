package com.fastcampus.housebatch.job.lawd;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.core.service.LawdService;
import org.assertj.core.util.Maps;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.batch.core.*;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {LawdInsertJobConfig.class, BatchTestConfig.class})
class LawdInsertJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @MockBean LawdService lawdService;

    @Test
    void success() throws Exception {
        // Given
        JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("TEST_LAWD_CODE.txt")));

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob(parameters);

        // Then
        assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        verify(lawdService, times(12)).upsert(any());
    }

    @Test
    void fail_whenFileNotFound() throws Exception {
        // When & Then
        JobParameters parameters = new JobParameters(Maps.newHashMap("filePath", new JobParameter("NOT_EXIST_FILE.txt")));
        Assertions.assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob(parameters));
        verify(lawdService, never()).upsert(any());
    }
}