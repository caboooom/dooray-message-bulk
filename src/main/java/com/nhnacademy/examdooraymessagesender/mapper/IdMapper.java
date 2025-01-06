package com.nhnacademy.examdooraymessagesender.mapper;

import com.nhnacademy.examdooraymessagesender.domain.SearchResult;
import com.nhnacademy.examdooraymessagesender.finder.Finder;
import java.util.AbstractMap;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author : 이성준
 * @since : 1.0
 */

@Component
@RequiredArgsConstructor
public class IdMapper {

    public static final String UNDEFINED_USER = "사용자를 특정할 수 없습니다. 존재하지 않거나 두 명이상 검색";
    private final Finder finder;

    public Map<Map.Entry<String, String>, String> mapId(Map<String, String> nameToMessage, String token) {
        Map<Map.Entry<String, String>, String> map = new HashMap<>();

        nameToMessage.forEach(
                (name, value) -> {
                    SearchResult searchResult = finder.findReceiver(name, token);

                    if (searchResult.getResult().size() == 1) {
                        SearchResult.Result receiverResult = searchResult.getResult().get(0);
                        map.put(new AbstractMap.SimpleEntry<>(receiverResult.getId(), name), value);
                    } else {
                        map.put(new AbstractMap.SimpleEntry<>(UNDEFINED_USER, name), value);
                    }

                }
        );

        return map;
    }
}
