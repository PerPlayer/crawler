package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.CodeEntry;
import com.crawler.dao.CodeEntryDao;
import com.crawler.repository.CodeEntryRepository;
import com.crawler.service.CacheService;
import com.crawler.service.CodeEntryService;
import com.crawler.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CodeEntryImpl implements CodeEntryService {

    @Autowired
    private CodeEntryRepository repository;

    @Autowired
    private CodeEntryDao dao;

    @Autowired
    private CacheService cacheService;

    @Override
    public CodeEntry save(@GroupValid CodeEntry codeEntry) {
        EntityUtil.init(codeEntry);
        return repository.save(codeEntry);
    }

    @Override
    public void update(String id, String column, Object value) {
        dao.update(id, column, value);
    }

    @Override
    public void update(CodeEntry codeEntry) {
        CodeEntry oldEntry = repository.findById(codeEntry.getId()).get();
        codeEntry.setVersion(oldEntry.getVersion());
        cacheService.put(codeEntry.getCode(), codeEntry);
        repository.save(codeEntry);
    }

    @Override
    public void delete(CodeEntry codeEntry) {
        repository.delete(codeEntry);
    }

    @Override
    public List<CodeEntry> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<CodeEntry> findAll(Pageable page) {
        return repository.findAll(page);
    }
    public CodeEntry findByCode(String code){
        CodeEntry codeEntry = repository.findByCode(code);
        cacheService.put(code, codeEntry);
        return codeEntry;
    }
}
