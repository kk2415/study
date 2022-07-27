package com.fastcampus.housebatch.core.entity;

import com.fastcampus.housebatch.core.dto.AptDealDto;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static javax.persistence.GenerationType.IDENTITY;

@ToString
@Setter
@Getter
@EntityListeners(AuditingEntityListener.class)
@Table(name = "apt_deal")
@Entity
public class AptDeal {

    @Id @GeneratedValue(strategy = IDENTITY)
    private Long aptDealId;

    @ManyToOne @JoinColumn(name = "apt_id") private Apt apt;
    @Column(nullable = false) private Double exclusiveArea;
    @Column(nullable = false) private LocalDate dealDate;
    @Column(nullable = false) private Long dealAmount;
    @Column(nullable = false) private Integer floor;
    @Column(nullable = false) private boolean dealCanceled;
    @Column private LocalDate dealCanceledDate;

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;
    @LastModifiedDate @Column(nullable = false) private LocalDateTime updatedAt;

    public static AptDeal of(AptDealDto aptDealDto, Apt apt) {
        AptDeal aptDeal = new AptDeal();

        aptDeal.setApt(apt);
        aptDeal.setExclusiveArea(aptDealDto.getExclusiveArea());
        aptDeal.setDealDate(aptDealDto.getDealDate());
        aptDeal.setDealAmount(aptDealDto.getDealAmount());
        aptDeal.setFloor(aptDealDto.getFloor());
        aptDeal.setDealCanceled(aptDealDto.isDealCanceled());
        aptDeal.setDealCanceledDate(aptDealDto.getDealCanceledDate());
        return aptDeal;
    }
}
