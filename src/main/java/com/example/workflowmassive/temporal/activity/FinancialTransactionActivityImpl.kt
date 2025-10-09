package com.example.workflowmassive.temporal.activity

import com.example.workflowmassive.temporal.workflow.TransactionRequest
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class FinancialTransactionActivityImpl: FinancialTransactionActivity {
    private val logger = LoggerFactory.getLogger(FinancialTransactionActivityImpl::class.java)
    override fun executeTransaction(request: TransactionRequest) {
        logger.info("executing transaction ${request.transactionId}")
    }
}