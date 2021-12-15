package com.tracing.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import lombok.Data;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Data
@Table(name="last_tracing")
public class LastTraceEntity {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @OneToOne
    private VehicleEntity vehicle;

    @OneToOne
    private TracingEntity trace;
}
