package com.kafka.consumer.demo.notification

import ma.glasnost.orika.MapperFacade
import org.slf4j.LoggerFactory
import org.springframework.data.repository.CrudRepository
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.listener.adapter.ConsumerRecordMetadata
import org.springframework.kafka.support.Acknowledgment
import org.springframework.messaging.handler.annotation.Payload
import org.springframework.stereotype.Component
import org.springframework.stereotype.Repository
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

enum class EventType { CREATE_NOTIFICATION, UPDATE_NOTIFICATION }

data class NotificationEvent(val key: Long, val notification: Notification, val type: EventType)

@Entity
data class Notification(
    @Id var id: Long? = null,
    var from: String = "",
    var to: String = ""
)

@Repository
interface NotificationH2Repository : CrudRepository<Notification, Long>

@Component
class Consumer(private val repository: NotificationH2Repository, private val mapper: MapperFacade) {

    private val logger = LoggerFactory.getLogger(javaClass)

    @KafkaListener(groupId = "\${spring.kafka.consumer.group-id}", topics = ["notification-event"])
    fun onMessage(@Payload payload: String, meta: ConsumerRecordMetadata, acknowledgment: Acknowledgment) =
        mapper.map(payload, NotificationEvent::class.java)?.notification
            // ?.apply { repository.save(this) }
            .run { acknowledgment.acknowledge() }
            .run { logger.info("[CONSUMER] Event from partition ${meta.partition()} with offset ${meta.offset()} is processed") }
}