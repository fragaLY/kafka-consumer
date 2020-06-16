package com.kafka.consumer.demo.notification

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.kafka.config.KafkaListenerEndpointRegistry
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.test.EmbeddedKafkaBroker
import org.springframework.kafka.test.utils.ContainerTestUtils
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class ConsumerIntegrationTest : IntegrationTest() {

    @field:Autowired
    lateinit var broker: EmbeddedKafkaBroker;

    @field:Autowired
    lateinit var template: KafkaTemplate<Long, String>

    @field:Autowired
    lateinit var registry: KafkaListenerEndpointRegistry

    @field:Autowired
    lateinit var repository: NotificationH2Repository

    @BeforeEach
    internal fun setUp() {
        registry.listenerContainers.forEach { ContainerTestUtils.waitForAssignment(it, broker.partitionsPerTopic) }
    }

    @Test
    @Transactional
    fun `test consume create event`() {
        // given
        val data =
            """{"key":1,"notification":{"id":null,"sender":"from","receiver":"to"},"type":"CREATE_NOTIFICATION"}"""
        template.sendDefault(data).get()
        val expected = Notification(1, "from", "to")

        // when
        CountDownLatch(1).await(1, TimeUnit.SECONDS)

        // then
        val notifications = repository.findAll().toList()
        assertTrue(notifications.size == 1)
        assertEquals(expected, notifications[0])
    }

    @Test
    @Transactional
    fun `test consume update event`() {
        // given
        val data =
            """{"key":1,"notification":{"id":1,"sender":"sender","receiver":"receiver"},"type":"UPDATE_NOTIFICATION"}"""
        template.sendDefault(data).get()
        val expected = Notification(1, "sender", "receiver")

        // when
        CountDownLatch(1).await(1, TimeUnit.SECONDS)

        // then
        val notifications = repository.findAll().toList()
        assertTrue(notifications.size == 1)
        assertEquals(expected, notifications[0])
    }
}