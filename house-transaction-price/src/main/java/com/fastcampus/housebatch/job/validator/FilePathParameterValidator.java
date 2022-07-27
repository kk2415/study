package com.fastcampus.housebatch.job.validator;

import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.JobParametersValidator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.StringUtils;

public class FilePathParameterValidator implements JobParametersValidator {

    private final static String FILE_PATH = "filePath";

    @Override
    public void validate(JobParameters parameters) throws JobParametersInvalidException {
        if (parameters == null) {
            throw new JobParametersInvalidException("파라미터가 null입니다.");
        }

        String parameter = parameters.getString(FILE_PATH);
        if (!StringUtils.hasText(parameter)) {
            throw new JobParametersInvalidException(FILE_PATH + "가 빈 문자열이거나 존재하지 않습니다.");
        }

        ClassPathResource resource = new ClassPathResource(parameter);
        if (!resource.exists()) {
            throw new JobParametersInvalidException(FILE_PATH + "가 클래스패스에 존재하지 않습니다. 경로를 확인해주세요");
        }

    }
}
