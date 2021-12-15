package com.tracing.services.scheduled.batch;

import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;

import lombok.Data;

@Component
@JobScope
@Data
public class BatchDataReaderBean {
    private String findLastTraceAfterTimeLimitQuery = "select l from LastTraceEntity l " +
        " inner join l.vehicle v " +
        " left join l.trace t    " +
        " where t.createdAt < :timeLimit ";

    private Map QueryParamValues = Collections.<String, Object>singletonMap(
        "timeLimit",Instant.now().minus(5, ChronoUnit.MINUTES)
    );
}
