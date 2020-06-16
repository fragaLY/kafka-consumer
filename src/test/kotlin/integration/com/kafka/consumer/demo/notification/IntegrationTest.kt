package com.kafka.consumer.demo.notification

import org.springframework.boot.test.context.SpringBootTest
import org.springframework.kafka.test.context.EmbeddedKafka
import org.springframework.test.context.TestPropertySource

@SpringBootTest
@EmbeddedKafka(topics = ["notification-event"], partitions = 3)
@TestPropertySource(
    properties = ["spring.kafka.producer.bootstrap-servers=\${spring.embedded.kafka.brokers}",
        "spring.kafka.consumer.bootstrap-servers=\${spring.embedded.kafka.brokers}"]
)
abstract class IntegrationTest
