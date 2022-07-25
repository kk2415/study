package com.fastcampus.hellospringbatch.core.service;

import com.fastcampus.hellospringbatch.dto.PlayerDto;
import com.fastcampus.hellospringbatch.dto.PlayerSalaryDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import java.time.Year;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerSalaryServiceTest {

    private PlayerSalaryService playerSalaryService;

    @BeforeEach
    public void setUp() {
        playerSalaryService = new PlayerSalaryService();
    }

    @Test
    public void calSalaryTest() {
        // Given
        Year mockYear = mock(Year.class);
        when(mockYear.getValue()).thenReturn(Year.now().getValue());

        Mockito.mockStatic(Year.class).when(Year::now).thenReturn(mockYear);

        PlayerDto mockPlayer = mock(PlayerDto.class);
        when(mockPlayer.getBirthYear()).thenReturn(1980);

        // When
        PlayerSalaryDto result = playerSalaryService.calSalary(mockPlayer);

        // Then
        Assertions.assertEquals(result.getSalary(),
                (Year.now().getValue() - mockPlayer.getBirthYear()) * 1_000_000);
    }

}