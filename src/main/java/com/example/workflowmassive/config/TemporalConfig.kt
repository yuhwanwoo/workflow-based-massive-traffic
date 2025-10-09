package com.example.workflowmassive.config

import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowClientOptions
import io.temporal.serviceclient.WorkflowServiceStubs
import io.temporal.serviceclient.WorkflowServiceStubsOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class TemporalConfig {
    @Value("\${temporal.connection.target}")
    private lateinit var temporalTarget: String

    @Value("\${temporal.namespace}")
    private lateinit var temporalNamespace: String

    companion object {
        const val FINANCE_TASK_QUEUE = "FINANCE_TASK_QUEUE"
    }

    @Bean
    fun workflowServiceStubs(): WorkflowServiceStubs {
        return WorkflowServiceStubs.newServiceStubs(
            WorkflowServiceStubsOptions.newBuilder()
                .setTarget(temporalTarget)
                .build()
        )
    }

    @Bean
    fun workflowClient(workflowServiceStubs: WorkflowServiceStubs): WorkflowClient {
        return WorkflowClient.newInstance(
            workflowServiceStubs,
            WorkflowClientOptions.newBuilder()
                .setNamespace(temporalNamespace)
                .build()
        )
    }

    @Bean
    fun workFactory(workflowClient: WorkflowClient): WorkerFactory {
        return WorkerFactory.newInstance(workflowClient)
    }

    @Bean
    fun financialWorker(workFactory: WorkerFactory): Worker {
        return workFactory.newWorker(FINANCE_TASK_QUEUE)
    }
}