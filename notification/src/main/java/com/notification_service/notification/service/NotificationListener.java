package com.notification_service.notification.service;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class NotificationListener {

    @KafkaListener(topics = "booking-notification-topic", groupId = "notification-group")
    public void handleNotification(String message) {
        System.out.println(" Notification Service received: " + message);
        // Optional: send email, SMS, push
    }
}
