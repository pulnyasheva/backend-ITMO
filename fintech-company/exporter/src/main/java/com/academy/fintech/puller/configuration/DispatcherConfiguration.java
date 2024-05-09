package com.academy.fintech.puller.configuration;

import com.academy.fintech.puller.core.service.dispatcher.property.ExportTaskRetryStalledProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({ExportTaskRetryStalledProperty.class})
public class DispatcherConfiguration {
}
