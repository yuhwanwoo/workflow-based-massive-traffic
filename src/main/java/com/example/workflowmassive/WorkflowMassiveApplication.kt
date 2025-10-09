package com.example.workflowmassive;

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.kafka.annotation.EnableKafka

@SpringBootApplication
@EnableKafka
class WorkflowMassiveApplication

fun main(args: Array<String>) {
    runApplication<WorkflowMassiveApplication>(*args)
}

