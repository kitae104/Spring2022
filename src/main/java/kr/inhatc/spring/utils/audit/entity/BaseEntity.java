package kr.inhatc.spring.utils.audit.entity;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.Getter;
import lombok.Setter;

@EntityListeners(value = {AuditingEntityListener.class})    // 감시 기능 적용
@MappedSuperclass   //자식 클래스에 매핑 정보만 제공하기 위해 사용
@Getter
@Setter
public abstract class BaseEntity extends BaseTimeEntity {

    @CreatedBy                    // 생성되어 저장될 때 시간을 자동으로 저장
    @Column(updatable = false)
    private String createdBy;

    @LastModifiedBy               // 값이 변경될 때 시간을 자동으로 저장
    private String modifiedBy;

}
