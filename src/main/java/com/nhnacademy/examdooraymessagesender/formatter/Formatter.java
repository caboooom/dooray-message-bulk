package com.nhnacademy.examdooraymessagesender.formatter;

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
public class Formatter {

    private static final String CRLF = "\r\n";

    public Map<String, String> format(Map<String, Map<String, String>> map) {
        Map<String, String> stringMap = new HashMap<>();

        map.forEach(
                (key, value) -> {
                    StringBuilder builder = new StringBuilder();

                    value.forEach(
                            (key1, value1) -> builder.append(key1).append(" : ").append(value1).append(CRLF)
                    );
                    stringMap.put(key, builder.toString());
                }
        );

        return stringMap;
    }
}
