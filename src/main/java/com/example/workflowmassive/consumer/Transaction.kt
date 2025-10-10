package com.example.workflowmassive.consumer

import com.example.workflowmassive.temporal.workflow.TransactionStatus
import com.fasterxml.jackson.annotation.JsonProperty
import java.time.LocalDateTime

data class Transaction(
    @JsonProperty("transactionId") val transactionId: String,
    @JsonProperty("fromAccountId") val fromAccountId: String?,
    @JsonProperty("toAccountId") val toAccountId: String?,

    @JsonProperty("transactionType") val transactionType: String,

    @JsonProperty("amount") val amount: String,


    @JsonProperty("description")
    val description: String,

    @JsonProperty("status")
    val status: TransactionStatus,

    @JsonProperty("created_at")
    val createdAt: LocalDateTime,

    @JsonProperty("updated_at")
    val updatedAt: LocalDateTime,
)

enum class TransactionType {
    @JsonProperty("DEPOSIT") DEPOSIT,
    @JsonProperty("WITHDRAW") WITHDRAW,
    @JsonProperty("TRANSFER") TRANSFER,
}