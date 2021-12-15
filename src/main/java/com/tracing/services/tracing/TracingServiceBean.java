package com.tracing.services.tracing;

import com.tracing.config.TracingProperties;
import com.tracing.entities.LastTraceEntity;
import com.tracing.entities.TracingEntity;
import com.tracing.entities.VehicleEntity;
import com.tracing.exceptions.ValidationException;
import com.tracing.repositories.tracing.LastTracingRepository;
import com.tracing.repositories.tracing.TracingRepository;
import com.tracing.repositories.vehicle.VehicleRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TracingServiceBean implements TracingService {

    private final VehicleRepository vehicleRepository;
    private final TracingRepository tracingRepository;
    private final LastTracingRepository lastTracingRepository;
    private final TracingProperties tracingProperties;


    @Override
    public void traceVehicleLocation(UUID vehicleId, float lon, float lat) {
        Instant currentInstant = Instant.now();
        VehicleEntity vehicle = fetchVehicle(vehicleId);
        Optional<TracingEntity> latestDuplicateTrace = fetchDuplicateTracesInTimeLimit(lon, lat, vehicleId, currentInstant);

        if (latestDuplicateTrace.isEmpty()) {
            TracingEntity newTrace = new TracingEntity();
            newTrace.setVehicle(vehicle);
            newTrace.setCreatedAt(Instant.now());
            newTrace.setLatitude(lat);
            newTrace.setLongitude(lon);
            TracingEntity lastTrace = tracingRepository.save(newTrace);
            Optional<LastTraceEntity> lastTraceLog = fetchLastTraceForVehicel(vehicleId);
            updateLastTraceLog(lastTraceLog, vehicle, lastTrace);
        }
    }

    private VehicleEntity fetchVehicle(UUID vehicleId) {
        return vehicleRepository.findVehicleById(vehicleId)
            .orElseThrow(() -> new ValidationException(
                "Vehicle not found"
            ));
    }

    private Optional<TracingEntity> fetchDuplicateTracesInTimeLimit(float lng, float lat, UUID vehicleId,
                                                                    Instant currentInstant) {
        return tracingRepository.findDuplicateTraces(vehicleId, lng, lat,
            currentInstant.minusSeconds(tracingProperties.getTimeInterval()));
    }

    private void updateLastTraceLog(Optional<LastTraceEntity> lastTraceLog, VehicleEntity vehicle, TracingEntity lastTrace) {
        if (lastTraceLog.isPresent()) {
            lastTraceLog.get().setTrace(lastTrace);
            lastTracingRepository.updateLastTrace(lastTraceLog.get());
        } else {
            LastTraceEntity newLastTraceLog = new LastTraceEntity();
            newLastTraceLog.setVehicle(vehicle);
            newLastTraceLog.setTrace(lastTrace);
            lastTracingRepository.save(newLastTraceLog);
        }
    }

    private Optional<LastTraceEntity> fetchLastTraceForVehicel(UUID vehicleId) {
        return lastTracingRepository.findLastTracingByVehicleId(vehicleId);
    }
}
