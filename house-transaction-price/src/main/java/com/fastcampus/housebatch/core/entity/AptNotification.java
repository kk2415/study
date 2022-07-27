package com.fastcampus.housebatch.core.entity;

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
@Table(name = "apt_notification")
@Entity
public class AptNotification {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long aptNotificationId;

    @Column(nullable = false) private String email;
    @Column(nullable = false) private String guLawdCd;
    @Column(nullable = false) private boolean enabled;

    @CreatedDate @Column(nullable = false) private LocalDateTime createdAt;
    @LastModifiedDate @Column(nullable = false) private LocalDateTime updatedAt;
}
