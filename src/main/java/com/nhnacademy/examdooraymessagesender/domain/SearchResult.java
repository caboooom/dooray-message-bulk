package com.nhnacademy.examdooraymessagesender.domain;

import java.util.List;
import lombok.Getter;

/**
 * @author : 이성준
 * @since : 1.0
 */
@Getter
public class SearchResult {
    List<SearchResult.Result> result;

    @Getter
    public static class Result {
        String id;
        String displayMemberId;
        String userCode;
        String name;
        String externalEmailAddress;
    }
}
