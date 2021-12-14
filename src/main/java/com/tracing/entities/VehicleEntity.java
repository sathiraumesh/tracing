package com.tracing.entities;

import java.time.Instant;
import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Data;

@Data
@Entity
public class VehicleEntity {

    @Id
    private UUID id;

    private Instant createdAt;

    private Instant updatedAt;
}
