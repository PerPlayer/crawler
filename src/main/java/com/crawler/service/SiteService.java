package com.crawler.service;

import com.crawler.crawler.model.Site;
import com.crawler.crawler.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface SiteService {

    Site save(Site site);

    void update(Site site);

    void delete(Site site);

    List<Site> findAll();

    Page<Site> findAll(Pageable page);

    Site findById(String id);
}
