package com.example.workflowmassive.consumer

import com.example.workflowmassive.service.TemporalWorkflowService
import com.example.workflowmassive.temporal.workflow.TransactionRequest
import com.fasterxml.jackson.databind.ObjectMapper
import io.grpc.netty.shaded.io.netty.handler.codec.base64.Base64Decoder
import org.slf4j.LoggerFactory
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.support.Acknowledgment
import org.springframework.kafka.support.KafkaHeaders
import org.springframework.messaging.handler.annotation.Header
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import java.math.BigDecimal
import java.nio.ByteBuffer
import java.util.Base64

@Component
class KafkaEventConsumer(
    private val objectMapper: ObjectMapper,
    private val temporalWorkflowService: TemporalWorkflowService
) {
    private val logger = LoggerFactory.getLogger(KafkaEventConsumer::class.java)

    @KafkaListener(topics = ["\${app.kafka.topics.customers}"])
    fun consumerEvents(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        ack: Acknowledgment
    ) {
        logger.info("Consuming topic $topic")
        ack.acknowledge()
    }

    @KafkaListener(topics = ["\${app.kafka.topics.accounts}"])
    fun consumerAccountEvents(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        ack: Acknowledgment
    ) {
        logger.info("Consuming topic $topic")
        ack.acknowledge()
    }

    @KafkaListener(topics = ["\${app.kafka.topics.transactions}"])
    fun consumerTransactionEvents(
        @Payload message: String,
        @Header(KafkaHeaders.RECEIVED_TOPIC) topic: String,
        @Header(KafkaHeaders.RECEIVED_PARTITION) partition: Int,
        @Header(KafkaHeaders.OFFSET) offset: Long,
        ack: Acknowledgment
    ) {
        logger.info("message: $message")

        val value = objectMapper.readValue(message, Transaction::class.java)
        logger.info("converted value: $value")

        startWorkflow(value)
        ack.acknowledge()
    }

    private fun startWorkflow(e: Transaction) {
        val transactionRequest = TransactionRequest(
            transactionId = e.transactionId,
            fromAccountId = e.fromAccountId,
            toAccountId = e.toAccountId,
            transactionType = e.transactionType.toString(),
            description = e.description,
            amount = decodeAmount(e.amount)
        )
        temporalWorkflowService.startTransactionWorkflow(transactionRequest)
    }

    private fun decodeAmount(encodedAmount: String?): BigDecimal {
        return try {
            if (encodedAmount.isNullOrBlank()) {
                BigDecimal.ZERO
            } else {
                val decodedBytes = Base64.getDecoder().decode(encodedAmount)
                val buffer = ByteBuffer.wrap(decodedBytes)

                return BigDecimal(buffer.long)
            }
        } catch (e: Exception) {
            BigDecimal.ZERO
        }
    }
}