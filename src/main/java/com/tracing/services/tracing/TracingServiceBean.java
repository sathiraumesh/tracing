package com.tracing.services.tracing;

import com.tracing.entities.VehicleEntity;
import com.tracing.entities.TracingEntity;
import com.tracing.exceptions.ValidationException;
import com.tracing.repositories.tracing.TracingRepository;
import com.tracing.repositories.vehicle.VehicleRepository;

import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TracingServiceBean implements VehicleTracingService{

    private final VehicleRepository vehicleRepository;
    private final TracingRepository tracingRepository;
    private final int TIME_LIMIT_IN_SECONDS = 30;

    @Override
    public void traceVehicleLocation(UUID vehicleId, float lon, float lat) {
        Instant currentInstant = Instant.now();
        VehicleEntity vehicle = fetchVehicle(vehicleId);
        Optional<TracingEntity> latestDuplicateTrace = fetchDuplicateTracesInTimeLimit(lon, lat, vehicleId,
            currentInstant, TIME_LIMIT_IN_SECONDS);

        if(!latestDuplicateTrace.isPresent()){
            TracingEntity newTrace = new TracingEntity();
            newTrace.setVehicle(vehicle);
            newTrace.setCreatedAt(Instant.now());
            newTrace.setLatitude(lat);
            newTrace.setLongitude(lon);
            tracingRepository.save(newTrace);
        }
    }

    private VehicleEntity fetchVehicle(UUID vehicleId){
        return vehicleRepository.findVehicleById(vehicleId)
            .orElseThrow(()-> new ValidationException(
                "Vehicle not found"
            ));
    }
    private Optional<TracingEntity> fetchDuplicateTracesInTimeLimit(float lng, float lat, UUID vehicleId,
                                                                    Instant currentInstant, int timeLimit){
        return tracingRepository.findDuplicateTraces(vehicleId, lng, lat, currentInstant.minusSeconds(timeLimit));
    }
}
