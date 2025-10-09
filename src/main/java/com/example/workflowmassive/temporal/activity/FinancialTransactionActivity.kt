package com.example.workflowmassive.temporal.activity

import com.example.workflowmassive.temporal.workflow.TransactionRequest
import io.temporal.activity.ActivityInterface
import io.temporal.activity.ActivityMethod

@ActivityInterface
interface FinancialTransactionActivity {
    @ActivityMethod
    fun executeTransaction(request: TransactionRequest)
}