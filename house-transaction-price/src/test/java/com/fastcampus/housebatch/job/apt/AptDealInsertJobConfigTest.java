package com.fastcampus.housebatch.job.apt;

import com.fastcampus.housebatch.BatchTestConfig;
import com.fastcampus.housebatch.adapter.ApartmentApiResource;
import com.fastcampus.housebatch.core.repository.LawdRepository;
import com.fastcampus.housebatch.core.service.AptDealService;
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
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBatchTest
@SpringBootTest
@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
@ContextConfiguration(classes = {AptDealInsertJobConfig.class, BatchTestConfig.class})
class AptDealInsertJobConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;
    @MockBean private AptDealService aptDealService;
    @MockBean private LawdRepository lawdRepository;
    @MockBean private ApartmentApiResource apartmentApiResource;

    @Test
    void success() throws Exception {
        // Given
        // when(lawdRepository.findDistinctGuLawdCd()).thenReturn(List.of("41135")); -> 이렇게 하면 ExitStatus가 FALE임... 왲;
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("41135", "41135"));
        when(apartmentApiResource.getResource(anyString(), any())).thenReturn(
                new ClassPathResource("test-api-response.xml")
        );

        // When
        JobExecution execution = jobLauncherTestUtils.launchJob(new JobParameters(Maps.newHashMap("yearMonth", new JobParameter("2022-01"))));

        // Then
        assertEquals(execution.getExitStatus(), ExitStatus.COMPLETED);
        verify(aptDealService, times(6)).upsert(any());
    }

    @Test
    void fail_whenYearMonthNotExist() throws Exception {
        // Given
        when(lawdRepository.findDistinctGuLawdCd()).thenReturn(Arrays.asList("41135"));
        when(apartmentApiResource.getResource(anyString(), any())).thenReturn(new ClassPathResource("test-api-response.xml"));

        // When
        assertThrows(JobParametersInvalidException.class, () -> jobLauncherTestUtils.launchJob());

        // Then
        verify(aptDealService, never()).upsert(any());
    }
}