package com.crawler.service;

import com.crawler.crawler.model.CodeEntry;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CodeEntryService {

    CodeEntry save(CodeEntry codeEntry);

    void update(String id, String column, Object value);

    void update(CodeEntry codeEntry);

    void delete(CodeEntry codeEntry);

    List<CodeEntry> findAll();

    Page<CodeEntry> findAll(Pageable page);

    CodeEntry findByCode(String code);
}
