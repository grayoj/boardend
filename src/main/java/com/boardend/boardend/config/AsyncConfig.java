package com.boardend.boardend.config;

import org.springframework.boot.autoconfigure.task.TaskExecutionProperties;
import org.springframework.boot.task.TaskExecutorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.annotation.EnableAsync;

@Configuration
@EnableAsync
public class AsyncConfig {

    @Bean(name = "taskExecutor")
    public TaskExecutor taskExecutor(TaskExecutionProperties properties, TaskExecutorBuilder builder) {
        return builder
                .corePoolSize(properties.getPool().getCoreSize())
                .maxPoolSize(properties.getPool().getMaxSize())
                .queueCapacity(properties.getPool().getQueueCapacity())
                .threadNamePrefix(properties.getThreadNamePrefix())
                .build();
    }
}