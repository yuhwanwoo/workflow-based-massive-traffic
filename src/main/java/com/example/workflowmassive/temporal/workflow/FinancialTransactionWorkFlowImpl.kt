package com.example.workflowmassive.temporal.workflow

import com.example.workflowmassive.temporal.activity.FinancialTransactionActivity
import io.temporal.activity.ActivityOptions
import io.temporal.common.RetryOptions
import io.temporal.workflow.Workflow
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Duration

@Component
class FinancialTransactionWorkFlowImpl: FinancialTransactionWorkFlow {
    private val logger = LoggerFactory.getLogger(FinancialTransactionWorkFlowImpl::class.java)
    private val opts = ActivityOptions.newBuilder()
        .setStartToCloseTimeout(Duration.ofMinutes(5))
        .setRetryOptions(
            RetryOptions.newBuilder()
                .setMaximumAttempts(3)
                .build()
        )
        .build()

    private val activities = Workflow.newActivityStub(FinancialTransactionActivity::class.java, opts)

    override fun processTransaction(request: TransactionRequest): TransactionResult {
        try {
            logger.info("processing transaction ${request.transactionId}")
            activities.executeTransaction(request)
            logger.info("finished transaction ${request.transactionId}")
        } catch (ex: Exception) {
            logger.error(ex.message, ex)
            return TransactionResult(
                transactionId = request.transactionId,
                status = TransactionStatus.FAILED,
                message = "거래 실패"
            )
        }
    }
}