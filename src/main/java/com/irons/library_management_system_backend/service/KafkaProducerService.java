package com.irons.library_management_system_backend.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
public class KafkaProducerService {

    private static final Logger logger = LoggerFactory.getLogger(KafkaProducerService.class);
    private static final String TOPIC = "library-transactions";

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate; // Spring's native tool to send messages

    public void publishEvent(String message) {
        logger.info("Broadcasting Event to Kafka Topic [{}]: {}", TOPIC, message);

        this.kafkaTemplate.send(TOPIC, message);
    }
}
