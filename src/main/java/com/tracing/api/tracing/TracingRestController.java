package com.tracing.api.tracing;

import com.tracing.domain.tracing.TraceLocation;
import com.tracing.services.tracing.TracingServiceBean;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "/tracing")
@AllArgsConstructor
public class TracingRestController {

private final TracingServiceBean tracingService;

    @PutMapping("/vehicle/{id}/position")
    public void traceVehicleTrace(
        @PathVariable UUID id,
        @RequestBody TraceLocation location
        ){
       tracingService.traceVehicleLocation(id, location.getLon(), location.getLat());
    }
}
