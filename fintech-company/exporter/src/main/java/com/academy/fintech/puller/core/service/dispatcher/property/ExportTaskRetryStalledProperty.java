package com.academy.fintech.puller.core.service.dispatcher.property;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "kafka.exporter.dispatcher.retry-stalled")
public record ExportTaskRetryStalledProperty (int passedMinutesInProcessingToRetry) { }
