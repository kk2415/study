package com.fastcampus.housebatch.core.dto;

import lombok.Builder;
import lombok.Getter;
import org.springframework.data.util.Pair;

import java.text.DecimalFormat;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@Getter
public class NotificationDto {

    private String email;
    private String guName;
    private Integer count;
    private List<AptDto> aptDtoList;

    public String toMessage() {
        DecimalFormat decimalFormat = new DecimalFormat();

        return String.format("%s 아파트 실거래 알림\n총 %d개의 거리가 발생했습니다.\n", guName, count)
            +
                aptDtoList.stream()
                    .map(aptDto -> String.format(" - %s : %s원\n", aptDto.getName(), decimalFormat.format(aptDto.getPrice())))
                    .collect(Collectors.joining());
    }
}
