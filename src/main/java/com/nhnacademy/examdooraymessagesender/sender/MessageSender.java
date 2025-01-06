package com.nhnacademy.examdooraymessagesender.sender;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nhnacademy.examdooraymessagesender.mapper.IdMapper;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

/**
 * @author : 이성준
 * @since : 1.0
 */

@Slf4j
@Component
public class MessageSender {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final Queue<Map.Entry<String, Map.Entry<String, String>>> queue;

    public MessageSender(RestTemplate restTemplate, ObjectMapper objectMapper) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.queue = new LinkedBlockingQueue<>();
    }

    public void send(Map<Map.Entry<String, String>, String> idAndNameEntryToFormattedMessage, String token) {
        idAndNameEntryToFormattedMessage.forEach(
                (idAndNameEntry, formattedMessage) -> {

                    String id = idAndNameEntry.getKey();

                    if (!id.equals(IdMapper.UNDEFINED_USER)) {
                        Map.Entry<String, String> id_MessageEntry =
                                new AbstractMap.SimpleEntry<>(idAndNameEntry.getKey(), formattedMessage);

                        Map.Entry<String, Map.Entry<String, String>> token_IdAndMessage =
                                new AbstractMap.SimpleEntry<>(token, id_MessageEntry);

                        queue.add(token_IdAndMessage);
                    } else {
                        log.warn("식별 불가 사용자 건너뛰기 : {}", idAndNameEntry.getValue());
                    }

                }
        );

        send();
    }

    private void send() {
        queue.forEach(
                token_IdAndMessage -> {
                    sendMessage(token_IdAndMessage.getKey(), token_IdAndMessage.getValue().getKey(),
                            token_IdAndMessage.getValue().getValue());
                }
        );
    }

    private void sendMessage(String token, String organizationMemberId, String message) {
        RequestEntity<String> request = RequestEntity
                .post("https://api.dooray.com/messenger/v1/channels/direct-send")
                .headers(
                        httpHeaders -> {
                            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                            httpHeaders.add("Authorization", "dooray-api " + token);
                        }
                ).body(buildIncomingMessage(organizationMemberId, message));

        log.debug("Sending Message: {}", request);

        ResponseEntity<String> response = restTemplate.exchange(request, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
            log.info("Dooray message sent successfully");
        }
    }

    private String buildIncomingMessage(String organizationMemberId, String message) {
        Map<String, String> payload = new HashMap<>();

        payload.put("organizationMemberId", organizationMemberId);
        payload.put("text", message);

        try {
            return objectMapper.writeValueAsString(payload);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


}
