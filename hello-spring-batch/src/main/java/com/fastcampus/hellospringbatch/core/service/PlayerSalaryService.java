package com.fastcampus.hellospringbatch.core.service;

import com.fastcampus.hellospringbatch.dto.PlayerDto;
import com.fastcampus.hellospringbatch.dto.PlayerSalaryDto;
import org.springframework.stereotype.Service;

import java.time.Year;

@Service
public class PlayerSalaryService {

    public PlayerSalaryDto calSalary(PlayerDto playerDto) {
        int salary = (Year.now().getValue() - playerDto.getBirthYear()) * 1_000_000;

        return PlayerSalaryDto.of(playerDto, salary);
    }

}
