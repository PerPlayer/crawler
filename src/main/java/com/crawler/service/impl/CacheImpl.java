package com.crawler.service.impl;

import com.crawler.exception.BusinessException;
import com.crawler.service.CacheService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CacheImpl implements CacheService {

    private Map<String, Object> cache = new HashMap<>();

    @Override
    public Object get(String key) {
        return cache.get(key);
    }

    @Override
    public void put(String key, Object value) {
        if (StringUtils.isEmpty(key)) {
            throw new BusinessException("The key is empty");
        }
        cache.put(key, value);
    }

    @Override
    public void delete(String key) {
        cache.remove(key);
    }

    @Override
    public void clear() {
        cache.clear();
    }
}
