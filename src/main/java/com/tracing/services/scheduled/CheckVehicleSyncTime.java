package com.tracing.services.scheduled;

import com.tracing.repositories.tracing.TracingRepository;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CheckVehicleSyncTime {

    private final TracingRepository tracingRepository;

    @Scheduled(fixedRate = 50000)
    public void syncVehicleTraces(){

    }

}
