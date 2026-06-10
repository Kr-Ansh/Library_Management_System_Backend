package com.irons.library_management_system_backend.integration;

import com.irons.library_management_system_backend.entities.Books;
import com.irons.library_management_system_backend.repository.BooksRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureWebMvc;
import org.springframework.cache.CacheManager;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
public class BooksControllerIT extends BaseIntegration{

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BooksRepository booksRepository;

    @MockitoBean
    private RedisTemplate<String, Object> redisTemplate;

    @MockitoBean
    private StringRedisTemplate stringRedisTemplate;

    @TestConfiguration
    static class TestCacheConfig {
        @Bean
        @Primary
        public CacheManager cacheManager() {
            return new ConcurrentMapCacheManager();
        }
    }

    @BeforeEach
    void cleanDatabase() {
        booksRepository.deleteAll();
    }

    @Test
    void testAndCreateAndGetBookIntegrationSuccess() throws Exception {

        Books newBook = new Books();
        newBook.setBookName("The Integration Vault");
        newBook.setBookAuthor("Docker Masters");
        newBook.setBookGenre("Tech Thriller");
        newBook.setIsBookBorrowed(false);

        Books savedBook = booksRepository.save(newBook);
        assertNotNull(savedBook.getBookId());

        mockMvc.perform(get("/api/books/id/" + savedBook.getBookId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.bookId").value(savedBook.getBookId()))
                .andExpect(jsonPath("$.bookName").value("The Integration Vault"))
                .andExpect(jsonPath("$.bookAuthor").value("Docker Masters"))
                .andExpect(jsonPath("$.bookGenre").value("Tech Thriller"));
    }
}
