package com.nhnacademy.examdooraymessagesender.repo;

import java.util.HashMap;
import java.util.Map;

/**
 * @author : 이성준
 * @since : 1.0
 */


public class MessageRepository<K, V> {

    Map<K, V> map;

    public MessageRepository() {
        this.map = new HashMap<>();
    }

    public V findById(K key) {
        return map.get(key);
    }

    public boolean save(K key, V value) {
        return map.put(key, value) == null;
    }
}
