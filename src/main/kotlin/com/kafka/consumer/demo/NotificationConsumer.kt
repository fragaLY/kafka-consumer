package com.kafka.consumer.demo

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/** @author Vadzim_Kavalkou */
@SpringBootApplication
class NotificationConsumer

fun main(args: Array<String>) {
	runApplication<NotificationConsumer>(*args)
}
