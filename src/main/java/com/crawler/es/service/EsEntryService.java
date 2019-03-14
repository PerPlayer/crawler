package com.crawler.es.service;

import com.crawler.crawler.model.Entry;
import com.crawler.es.document.EntryDocument;
import com.crawler.util.BPage;
import org.elasticsearch.action.search.SearchResponse;

public interface EsEntryService {

    void save(EntryDocument document);

    void deleteById(String id);

    BPage<Entry> queryWithHighLight(String content, BPage<Entry> page);

}
