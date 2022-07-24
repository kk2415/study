package study.datajpa.entity;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public class BaseEntityV3 extends BaseTimeEntity {

    /**
     * 생성자, 수성자 정보작 필요없는 테이블은 BaseTimeEntity를 쓰고 필요한 테이블은 BaseEntityV3를 이용
     * */
    @Column(updatable = false)
    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

}
