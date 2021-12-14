package com.tracing.services.tracing;

import java.util.UUID;

public interface VehicleTracingService {
    void traceVehicleLocation(UUID vehicleID, float longitude, float latitude);
}
