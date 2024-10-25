package com.interview.etravli.models;

import com.interview.etravli.listener.DefaultAuditListener;
import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.envers.NotAudited;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(DefaultAuditListener.class)
public abstract class Auditable<U> {

    @CreatedDate
    @Column(name="created_date")
    @NotAudited
    protected LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name="modified_date")
    protected LocalDateTime modifiedAt;

    @CreatedBy
    @Column(name="created_by", columnDefinition = "CHAR(36)")
    @NotAudited
    @JdbcTypeCode(SqlTypes.VARCHAR)
    protected U createdBy;

    @LastModifiedBy
    @Column(name="modified_by", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    protected U modifiedBy;

}