package com.fastcampus.housebatch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class DealDateParameterValidator implements JobParametersValidator {

    private final static String DEAL_DATE = "dealDate";

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("파라미터가 null입니다.");
        }

        String parameter = parameters.getString(DEAL_DATE);
        if (!StringUtils.hasText(parameter)) {
            throw new JobParametersInvalidException(DEAL_DATE + "가 빈 문자열이거나 존재하지 않습니다.");
        }

        try {
            LocalDate.parse(parameter);
        } catch (DateTimeParseException e) {
            throw new JobParametersInvalidException(DEAL_DATE + "가 올바른 날짜 형식이 아닙니다. yyyy-MM-dd이어야 합니다.");
        }
    }
}
