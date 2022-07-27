package com.fastcampus.housebatch.core.dto;

import io.micrometer.core.instrument.util.StringUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

/**
 * 아파트 실거래 API Response의 각 항목을 담고있는 객체
 * */
@ToString
@Getter
@XmlRootElement(name = "item")
public class AptDealDto {

    @XmlElement(name = "거래금액")
    private String dealAmount;

    public Long getDealAmount() {
        return Long.parseLong(dealAmount.replaceAll(",", "").trim());
    }

    @XmlElement(name = "건축년도")
    private Integer builtYear;

    @XmlElement(name = "년")
    private Integer year;

    @XmlElement(name = "법정동")
    private String dong;

    @XmlElement(name = "아파트")
    private String aptName;

    @XmlElement(name = "월")
    private Integer month;

    @XmlElement(name = "일")
    private Integer day;

    @XmlElement(name = "전용면적")
    private Double exclusiveArea;

    @XmlElement(name = "지번")
    private String jibun;

    public String getJibun() {
        return Optional.ofNullable(jibun).orElse("");
    }

    @XmlElement(name = "지역코드")
    private String regionalCode;

    @XmlElement(name = "층")
    private Integer floor;

    @XmlElement(name = "해제사유발생일")
    private String dealCanceledDate; //22.01.01

    public LocalDate getDealCanceledDate() {
        if (StringUtils.isBlank(dealCanceledDate)) {
            return null;
        }
        return LocalDate.parse(dealCanceledDate.trim(), DateTimeFormatter.ofPattern("yy.MM.dd"));
    }

    @XmlElement(name = "해제여부")
    private String dealCanceled; //O

    public boolean isDealCanceled() {
        return "O".equals(dealCanceled.trim());
    }

    public LocalDate getDealDate() {
        return LocalDate.of(year, month, day);
    }

}
