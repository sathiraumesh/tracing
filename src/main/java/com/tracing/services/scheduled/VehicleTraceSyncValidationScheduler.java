package com.tracing.services.scheduled;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class VehicleTraceSyncValidationScheduler {


    private final JobLauncher jobLauncher;

    private final Job job;

    @Scheduled(fixedRate = 5 * 60000)
    public void syncVehicleTraces() throws Exception {
        JobParameters jobParameters = new JobParametersBuilder()
            .addLong("uniqueness", System.currentTimeMillis())
            .toJobParameters();
        jobLauncher.run(job, jobParameters);
    }
}
