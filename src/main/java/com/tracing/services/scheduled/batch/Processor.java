package com.tracing.services.scheduled.batch;

import com.tracing.entities.LastTraceEntity;
import com.tracing.entities.TracingEntity;

import org.springframework.batch.item.ItemProcessor;

import java.time.Instant;

public class Processor implements ItemProcessor<LastTraceEntity, TracingEntity> {
    @Override
    public TracingEntity process(LastTraceEntity data) throws Exception {
        TracingEntity trace = new TracingEntity();
        trace.setVehicle(data.getVehicle());
        trace.setCreatedAt(Instant.now());
        return trace;
    }
}
