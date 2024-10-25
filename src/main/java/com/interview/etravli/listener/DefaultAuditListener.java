package com.interview.etravli.listener;

import com.interview.etravli.models.Auditable;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;

import java.time.LocalDateTime;

public class DefaultAuditListener {

    @PrePersist
    public void setCreatedAt(Object entity){
        if(entity != null){
            if(entity instanceof Auditable<?>) {
                ((Auditable<?>) entity).setCreatedAt(LocalDateTime.now());
            }
        }
    }

    @PreUpdate
    public void setModifiedAt(Object entity){
        if(entity != null){
            if(entity instanceof Auditable<?>) {
                ((Auditable<?>) entity).setModifiedAt(LocalDateTime.now());
            }
        }
    }

}