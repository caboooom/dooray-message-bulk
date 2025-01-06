package com.nhnacademy.examdooraymessagesender.config;

import com.nhnacademy.examdooraymessagesender.repo.MessageRepository;
import java.util.Map;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : 이성준
 * @since : 1.0
 */

@Configuration
public class RepositoryBeanConfig {

    @Bean
    public MessageRepository<String, Map<Map.Entry<String, String>, String>> messageRepository() {
        return new MessageRepository<>();
    }
}
