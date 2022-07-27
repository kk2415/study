package com.fastcampus.housebatch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.util.StringUtils;

public class LawdCdParameterValidator implements JobParametersValidator {

    private static final String LAWD_CD = "lawdCd";

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException(LAWD_CD + "가 null 입니다.");
        }


        String parameter = parameters.getString(LAWD_CD);

        if (isNotValid(parameter)) {
            throw new JobParametersInvalidException(parameter + "는 5자리 정수가 아닙니다.");
        }
    }

    private boolean isNotValid(String parameter) {
        return !isValid(parameter);
    }

    private boolean isValid(String parameter) {
        return StringUtils.hasText(parameter) &&
                parameter.length() == 5 &&
                parameter.chars().allMatch(Character::isDigit);
    }
}
