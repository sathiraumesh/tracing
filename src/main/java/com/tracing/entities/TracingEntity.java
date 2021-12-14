package com.tracing.entities;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import lombok.Data;

@Entity
@Data
public class TracingEntity {
    @Id
    private long id;

    @ManyToOne
    private VehicleEntity vehicle;

    private float latitude;

    private float longitude;

    private Instant createdAt;
}
