package com.nhnacademy.examdooraymessagesender.finder;

import com.nhnacademy.examdooraymessagesender.domain.SearchResult;
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

@Component
@Slf4j
@RequiredArgsConstructor
public class Finder {

    private final RestTemplate restTemplate;

    public SearchResult findReceiver(String name, String token) {
        RequestEntity<Void> request = RequestEntity
                .get("https://api.dooray.com/common/v1/members?name=" + name)
                .headers(
                        httpHeaders -> {
                            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
                            httpHeaders.add("Authorization", "dooray-api " + token);
                        }
                )
                .build();

        ResponseEntity<SearchResult> response = restTemplate.exchange(request, SearchResult.class);

        if (response.getStatusCode().is2xxSuccessful() && response.getBody().getResult().size() != 1) {
            log.debug("Found more than one receiver with name {}", name);
        }

        return response.getBody();
    }
}
