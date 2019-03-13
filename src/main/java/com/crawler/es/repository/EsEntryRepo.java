package com.crawler.es.repository;

import com.crawler.es.document.EntryDocument;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Component;

@Component
public interface EsEntryRepo extends ElasticsearchRepository<EntryDocument, String> {
}
