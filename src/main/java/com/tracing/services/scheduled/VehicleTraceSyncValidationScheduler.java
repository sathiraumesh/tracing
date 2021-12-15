package com.tracing.services.scheduled;

import com.tracing.repositories.tracing.TracingRepository;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor

public class VehicleTraceSyncValidationScheduler {

    @Autowired
    JobLauncher jobLauncher;

    @Autowired
    Job job;

    private final TracingRepository tracingRepository;

    @Scheduled(fixedRate = 5*60000)
    public void syncVehicleTraces() throws Exception{
        JobParameters jobParameters = new JobParametersBuilder().addLong("uniqueness", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
