package com.fastcampus.hellospringbatch.job.parallel;

import com.fastcampus.hellospringbatch.dto.AmountDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class AmountFieldSetMapper implements FieldSetMapper<AmountDto> {

    @Override
    public AmountDto mapFieldSet(FieldSet fieldSet) throws BindException {
        AmountDto amountDto = new AmountDto();
        amountDto.setIndex(fieldSet.readInt(0));
        amountDto.setName(fieldSet.readString(1));
        amountDto.setAmount(fieldSet.readInt(2));

        return amountDto;
    }

}
