package com.crawler.service;

public interface CacheService {

    Object get(String key);

    void put(String key, Object value);

    void delete(String key);

    void clear();
}
