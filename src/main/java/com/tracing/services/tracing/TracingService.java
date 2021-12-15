package com.tracing.services.tracing;

import java.util.UUID;

public interface TracingService {
    void traceVehicleLocation(UUID vehicleID, float lon, float lat);
}
