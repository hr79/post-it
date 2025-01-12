package com.example.postItBackend.model;

import jakarta.persistence.*;
import lombok.Getter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
@Getter
public abstract class BaseEntity {

    @Column(nullable = false, updatable = false) private ZonedDateTime createdAt;

    @Column(nullable = true) private ZonedDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        ZoneId zoneId = ZoneId.of("Asia/Seoul");
//        System.out.println("==== zone id: " + zoneId);
        this.createdAt = ZonedDateTime.now(zoneId);
//        System.out.println("==== createdAt = "+createdAt);
    }
}
