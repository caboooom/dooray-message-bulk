package com.nhnacademy.examdooraymessagesender.parser;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import org.springframework.stereotype.Component;

/**
 * @author : 이성준
 * @since : 1.0
 */

@Component
public class CSVParser {

    public Map<String, Map<String, String>> parse(String charSequence) {
        charSequence = charSequence.replaceAll("\\r\\n", "\n");


        Queue<String> rows = new LinkedList<>(List.of(charSequence.split("\n")));

        String header = rows.remove();

        String[] headings = header.split(",");

        String[] columnHeadings = Arrays.copyOfRange(headings, 1, headings.length);

        Map<String, Map<String, String>> messages = new HashMap<>();

        for (String row : rows) {
            String[] columns = row.split(",");

            String receiver = columns[0];
            String[] messageValues = Arrays.copyOfRange(columns, 1, headings.length);
            Map<String, String> body = parseRow(columnHeadings, messageValues);

            messages.put(receiver, body);
        }

        return messages;
    }

    private Map<String, String> parseRow(String[] columnHeadings, String... columns) {
        Map<String, String> messageBody = new HashMap<>();

        for (int i = 0, j = 0; i < columnHeadings.length || j < columns.length; i++, j++) {
            messageBody.put(columnHeadings[i], columns[j]);
        }

        return messageBody;
    }
}
