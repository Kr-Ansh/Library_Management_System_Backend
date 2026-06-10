package com.irons.library_management_system_backend.integration;

import com.irons.library_management_system_backend.repository.BooksRepository;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class BookKafkaIT extends BaseIntegration {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BooksRepository booksRepository;

    private Consumer<String, String> testConsumer;

    @TestConfiguration
    static class TestCacheConfig {
        @Bean
        @Primary
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }
    }

    @BeforeEach
    void setUpKafkaConsumer() {

        Map<String, Object> consumerProps = new HashMap<>();
        consumerProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaContainer.getBootstrapServers());
        consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, "test-group");
        consumerProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "true");
        consumerProps.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumerProps.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        consumerProps.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);

        DefaultKafkaConsumerFactory<String, String> consumerFactory = new DefaultKafkaConsumerFactory<>(consumerProps);
        testConsumer = consumerFactory.createConsumer();

        testConsumer.subscribe(Collections.singletonList("library-transactions"));
    }

    @AfterEach
    void tearDown() {
        if (testConsumer != null) {
            testConsumer.close();
        }
        booksRepository.deleteAll();
    }

    @Test
    void testBookCreationTriggersKafkaEventSuccess() throws Exception {

        String bookJsonPayload = """
                {
                    "bookName": "Kafka Chronicles",
                    "bookAuthor": "Event Driven Masters",
                    "bookGenre": "System Architecture",
                    "isBookBorrowed": false
                }
                """;

        mockMvc.perform(post("/api/books/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(bookJsonPayload))
                .andExpect(status().isCreated());

        ConsumerRecord<String, String> receivedRecord = KafkaTestUtils.getSingleRecord(testConsumer, "library-transactions");

        assertNotNull(receivedRecord);
        assertNotNull(receivedRecord.value());
        System.out.println("🔥 Real Kafka Event Intercepted: " + receivedRecord.value());
    }
}
