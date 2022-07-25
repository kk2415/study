package com.fastcampus.hellospringbatch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

@Component
public class LocalDateParameterValidator implements JobParametersValidator {

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("날짜를 입력해주세요.");
        }

        String parameter = parameters.getString("targetDate");

        if (!StringUtils.hasText(parameter)) {
            throw new JobParametersInvalidException(parameter + "가 빈 문자열이거나 공백입니다.");
        }

        try {
            LocalDate.parse(parameter);
        } catch (DateTimeParseException e) {
            throw new JobParametersInvalidException(parameter + "는 유효하지 않은 날짜 형식입니다.");
        }
    }

}
