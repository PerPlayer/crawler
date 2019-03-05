package com.crawler.repository;

import com.crawler.crawler.model.Entry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntryRepository extends JpaRepository<Entry, String> {
}
