package com.crawler.repository;

import com.crawler.crawler.model.Entry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, String> {

    Page<Entry> findAllByContentLike(String content, Pageable pageable);
}
