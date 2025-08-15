package com.bookingmanagement.booking_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

//@EnableFeignClients(basePackages = "com.bookingmanagement.booking_service.client")
@EnableFeignClients
@EnableCaching
@SpringBootApplication
public class BookingServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(BookingServiceApplication.class, args);
		System.out.println("Booking Service started");
	}

//	@Bean
//	public NewTopic bookingNotificationTopic() {
//		return TopicBuilder.name("booking-notification-topic")
//				.partitions(1)
//				.replicas(1)
//				.build();
//	}
}
