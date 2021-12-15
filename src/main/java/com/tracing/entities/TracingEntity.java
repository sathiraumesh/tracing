package com.tracing.entities;

import java.time.Instant;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import io.micrometer.core.lang.Nullable;
import lombok.Data;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name="tracing")
public class TracingEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private VehicleEntity vehicle;

    @Nullable
    private Float latitude;

    @Nullable
    private Float longitude;

    private Instant createdAt;
}
