package com.example.workflowmassive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.EnableKafka;

@SpringBootApplication
@EnableKafka
public class WorkflowMassiveApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkflowMassiveApplication.class, args);
    }

}
