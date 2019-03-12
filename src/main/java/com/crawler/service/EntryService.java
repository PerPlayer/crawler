package com.crawler.service;

import com.crawler.crawler.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface EntryService {

    Entry save(Entry entry);

    void update(String id, String column, Object value);

    void update(Entry entry);

    void delete(Entry entry);

    List<Entry> findAll();

    Page<Entry> findAll(Pageable page);

    Page<Entry> findAllByContent(String content, Pageable pageable);

    Entry findById(String id);
}
