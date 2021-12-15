package com.tracing.config;

import com.tracing.entities.LastTraceEntity;
import com.tracing.entities.TracingEntity;
import com.tracing.services.scheduled.batch.BatchDataReaderBean;
import com.tracing.services.scheduled.batch.BatchJobCompletionListener;
import com.tracing.services.scheduled.batch.Processor;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JpaItemWriter;
import org.springframework.batch.item.database.JpaPagingItemReader;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.sql.DataSource;

import lombok.AllArgsConstructor;

@Configuration
@EnableBatchProcessing
@AllArgsConstructor
public class BatchConfig {

    private final DataSource dataSource;

    private final TracingProperties tracingProperties;

    private final BatchDataReaderBean batchDataReaderBean;

    private final EntityManager em;

    private final JobBuilderFactory jobBuilderFactory;

    private final StepBuilderFactory stepBuilderFactory;

    @Bean
    @JobScope
    public JpaPagingItemReader<LastTraceEntity> reader() throws Exception {
        // scoped bean is created since the time has to change in every job
        Map QueryParamValues = Collections.<String, Object>singletonMap(
            batchDataReaderBean.getQueryParamName(), Instant.now().minus(tracingProperties.getTimeLimit(), ChronoUnit.MINUTES)
        );
        JpaPagingItemReader<LastTraceEntity> reader = new JpaPagingItemReader<LastTraceEntity>();
        reader.setQueryString(batchDataReaderBean.getFindLastTraceAfterTimeLimitQuery());
        reader.setParameterValues(QueryParamValues);
        reader.setEntityManagerFactory(em.getEntityManagerFactory());
        reader.setPageSize(100);
        reader.afterPropertiesSet();
        reader.setSaveState(true);
        return reader;
    }

    @Bean
    public Job validateTraceSync(JobBuilderFactory jobs, Step s1) {
        return jobs.get("validateTraceSync")
            .incrementer(new RunIdIncrementer()) // because a spring config bug, this incrementer is not really useful
            .flow(s1)
            .end()
            .build();
    }

    @Bean
    public ItemWriter<TracingEntity> writer() {
        JpaItemWriter writer = new JpaItemWriter<TracingEntity>();
        writer.setEntityManagerFactory(em.getEntityManagerFactory());
        return writer;
    }


    @Bean
    public Step step1(StepBuilderFactory stepBuilderFactory, ItemReader<LastTraceEntity> reader,
                      ItemWriter<TracingEntity> writer, ItemProcessor<LastTraceEntity, TracingEntity> processor) {
        return stepBuilderFactory.get("step1")
            .<LastTraceEntity, TracingEntity>chunk(1000)
            .reader(reader)
            .processor(processor)
            .writer(writer)
            .build();
    }

    @Bean
    public ItemProcessor<LastTraceEntity, TracingEntity> processor() {
        return new Processor();
    }

    @Bean
    public JobExecutionListener listener() {
        return new BatchJobCompletionListener();
    }
}

