package com.fastcampus.hellospringbatch.job.player;

import com.fastcampus.hellospringbatch.dto.PlayerDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

public class PlayerFieldSetMapper implements FieldSetMapper<PlayerDto> {

    @Override
    public PlayerDto mapFieldSet(FieldSet fieldSet) throws BindException {
        PlayerDto playerDto = new PlayerDto();
        playerDto.setID(fieldSet.readString(0));
        playerDto.setLastName(fieldSet.readString(1));
        playerDto.setFirstName(fieldSet.readString(2));
        playerDto.setPosition(fieldSet.readString(3));
        playerDto.setBirthYear(fieldSet.readInt(4));
        playerDto.setDebutYear(fieldSet.readInt(5));

        return playerDto;
    }

}
