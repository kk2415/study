package com.fastcampus.hellospringbatch.job;

import com.fastcampus.hellospringbatch.BatchTestConfig;
import com.fastcampus.hellospringbatch.core.domain.PlainText;
import com.fastcampus.hellospringbatch.core.repository.PlainTextRepository;
import com.fastcampus.hellospringbatch.core.repository.ResultTextRepository;
import org.junit.jupiter.api.AfterEach;
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

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {PlainTextJobConfig.class, BatchTestConfig.class})
class PlainTextJobConfigTest {


    private final PlainTextRepository plainTextRepository;
    private final ResultTextRepository resultTextRepository;
    private final JobLauncherTestUtils jobLauncherTestUtils;

    public PlainTextJobConfigTest(@Autowired PlainTextRepository plainTextRepository,
                                  @Autowired ResultTextRepository resultTextRepository,
                                  @Autowired JobLauncherTestUtils jobLauncherTestUtils) {
        this.plainTextRepository = plainTextRepository;
        this.resultTextRepository = resultTextRepository;
        this.jobLauncherTestUtils = jobLauncherTestUtils;
    }

    @AfterEach
    public void tearDown() {
        plainTextRepository.deleteAll();
        resultTextRepository.deleteAll();
    }

    @Test
    void success_givenNonPlainText() throws Exception {
        // Given

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        assertEquals(resultTextRepository.count(), 0);
    }

    @Test
    void success_givenPlainText() throws Exception {
        // Given
        givenPlainText(12);

        // When
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        // Then
        assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        assertEquals(resultTextRepository.count(), 12);
    }

    private void givenPlainText(Integer count) {
        IntStream.range(0, count)
                .forEach(num -> {
                    plainTextRepository.save(new PlainText(null, "text" + num));
                });
    }


}