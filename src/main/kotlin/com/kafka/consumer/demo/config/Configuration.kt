package com.kafka.consumer.demo.config

import ma.glasnost.orika.MapperFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.annotation.EnableKafka

@Configuration
@EnableKafka
class NotificationEventsConsumer {
}

@Configuration
class Mapper {

}