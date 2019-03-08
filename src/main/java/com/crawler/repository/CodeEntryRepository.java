package com.crawler.repository;

import com.crawler.crawler.model.CodeEntry;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CodeEntryRepository extends JpaRepository<CodeEntry, String> {

    CodeEntry findByCode(String code);
}
