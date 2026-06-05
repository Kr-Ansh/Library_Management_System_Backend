package com.irons.library_management_system_backend.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic libraryTransactionsTopic() {
        return TopicBuilder.name("library-transactions")
                .partitions(1) // Single storage lane for our baseline stream
                .replicas(1)   // Single broker instance replication checkpoint
                .build();
    }
}
