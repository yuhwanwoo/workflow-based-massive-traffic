package com.example.workflowmassive.temporal.workflow

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import io.temporal.workflow.WorkflowInterface
import io.temporal.workflow.WorkflowMethod
import java.math.BigDecimal

/**
 * 실행이될 워크플로우 정의
 * 최소 1개의 워크플로우 메소드가 있어야함 -> 엔트리 포인트
 */
@WorkflowInterface
interface FinancialTransactionWorkFlow {

    @WorkflowMethod
    fun processTransaction(request: TransactionRequest): TransactionResult
}

data class TransactionRequest @JsonCreator constructor(
    @JsonProperty("transactionId") val transactionId: String,
    @JsonProperty("fromAccountId") val fromAccountId: String?,
    @JsonProperty("toAccountId") val toAccountId: String?,
    @JsonProperty("amount") val amount: BigDecimal,
    @JsonProperty("transactionType") val transactionType: String,
    @JsonProperty("description") val description: String? = null
)

data class TransactionResult @JsonCreator constructor(
    @JsonProperty("transactionId") val transactionId: String,
    @JsonProperty("status") val status: TransactionStatus,
    @JsonProperty("message") val message: String,
    @JsonProperty("processedAt") val processedAt: Long = System.currentTimeMillis()
)

enum class TransactionStatus {
    PENDING,
    VALIDATING,
    PROCESSING,
    COMPLETED,
    FAILED,
}
