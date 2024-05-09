package com.academy.fintech.dwh;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.annotation.FullyQualifiedAnnotationBeanNameGenerator;
import org.springframework.kafka.annotation.EnableKafka;


@SpringBootApplication()
@EnableKafka
public class Application {
    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .beanNameGenerator(new FullyQualifiedAnnotationBeanNameGenerator())
                .run(args);
    }

}
