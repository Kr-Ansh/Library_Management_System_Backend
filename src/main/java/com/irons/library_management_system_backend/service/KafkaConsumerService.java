package com.irons.library_management_system_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
public class KafkaConsumerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaConsumerService.class);

    // @KafkaListener tells Spring to constantly listen to the specified topic.
    // groupId groups multiple instances together for load balancing.
    @KafkaListener(topics = "library-transactions", groupId = "library-audit-group")
    public void consumeEvent(String message) {
        logger.info("Success! Intercepted Message from Kafka Event Stream: {}", message);

        // This is where our future Notification or Security engine logic will live!
    }
}
