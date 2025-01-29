package com.example.foody.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Represents the base entity with common fields for all entities.
 * <p>
 * Provides fields for creation and deletion timestamps.
 */
@Data
@MappedSuperclass
public class DefaultEntity {

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    /**
     * Marks the entity as deleted by setting the deletedAt timestamp to the current time.
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}