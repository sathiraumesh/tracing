package com.tracing.services.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CheckVehicleSyncTime {

    @Scheduled(fixedRate = 50000)
    public void syncVehicleTraces(){
        System.out.println("lgging");
    }

}
