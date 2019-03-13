package com.crawler.es.service.impl;

import com.crawler.es.document.EntryDocument;
import com.crawler.es.repository.EsEntryRepo;
import com.crawler.es.service.EsEntryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EsEntryImpl implements EsEntryService {

    @Autowired
    private EsEntryRepo repo;

    @Override
    public void save(EntryDocument document) {
        repo.save(document);
    }
}
