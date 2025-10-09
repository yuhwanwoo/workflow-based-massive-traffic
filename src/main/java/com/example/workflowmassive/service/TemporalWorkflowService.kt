package com.example.workflowmassive.service

import com.example.workflowmassive.config.TemporalConfig
import com.example.workflowmassive.temporal.activity.FinancialTransactionActivityImpl
import com.example.workflowmassive.temporal.workflow.FinancialTransactionWorkFlow
import com.example.workflowmassive.temporal.workflow.FinancialTransactionWorkFlowImpl
import com.example.workflowmassive.temporal.workflow.TransactionRequest
import io.temporal.client.WorkflowClient
import io.temporal.client.WorkflowOptions
import io.temporal.worker.Worker
import io.temporal.worker.WorkerFactory
import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.logging.Logger

class TemporalWorkflowService(
    private val workflowClient: WorkflowClient,
    private val workerFactory: WorkerFactory,
    private val financialWorker: Worker,
    private val financialTransactionActivityImpl: FinancialTransactionActivityImpl
) {
    private val logger = LoggerFactory.getLogger(TemporalWorkflowService::class.java)

    @PostConstruct
    fun registerWorkflowAndActivities() {
        financialWorker.registerWorkflowImplementationTypes(FinancialTransactionWorkFlowImpl::class.java)
        financialWorker.registerActivitiesImplementations(financialTransactionActivityImpl)

        runCatching {
            workerFactory.start()
            logger.info("Started workflow worker")
        }.onFailure { ex ->
            logger.error("Failed to start workflow worker", ex)
        }
    }

    fun startTransactionWorkflow(request: TransactionRequest): String {
        runCatching {
            val opts = WorkflowOptions.newBuilder()
                .setTaskQueue(TemporalConfig.FINANCE_TASK_QUEUE)
                .setWorkflowId("transaction-${request.transactionId}")
                .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
                .build()
            val workflow = workflowClient.newWorkflowStub(FinancialTransactionWorkFlow::class.java, opts)

            WorkflowClient.start(workflow::processTransaction, request)

            logger.info("workflow 시작됨!! transaction-${request.transactionId}")

            return "success"
        }.getOrElse { ex -> throw ex }
//        val opts = WorkflowOptions.newBuilder()
//            .setTaskQueue(TemporalConfig.FINANCE_TASK_QUEUE)
//            .setWorkflowId("transaction-${request.transactionId}")
//            .setWorkflowExecutionTimeout(Duration.ofMinutes(10))
//            .build()
//        val workflow = workflowClient.newWorkflowStub(FinancialTransactionWorkFlow::class.java, opts)
//
//        WorkflowClient.start(workflow::processTransaction, request)
//
//        logger.info("workflow 시작됨!! transaction-${request.transactionId}")
//
//        return ""
    }
}