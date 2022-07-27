package com.fastcampus.housebatch.core.entity;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@ToString
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "apt")
@Entity
public class Apt {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long aptId;

    @Column(nullable = false)
    private String aptName;

    @Column(nullable = false)
    private String jibun;

    @Column(nullable = false)
    private String dong;

    @Column(nullable = false)
    private String guLawdCd;

    @Column(nullable = false)
    private Integer builtYear;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public static Apt from(AptDealDto aptDealDto) {
        Apt apt = new Apt();

        apt.setAptName(aptDealDto.getAptName().trim());
        apt.setJibun(aptDealDto.getJibun().trim());
        apt.setDong(aptDealDto.getDong().trim());
        apt.setGuLawdCd(aptDealDto.getRegionalCode().trim());
        apt.setBuiltYear(aptDealDto.getBuiltYear());
        return apt;
    }

}
