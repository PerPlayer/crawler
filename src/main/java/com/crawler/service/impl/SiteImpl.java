package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.Site;
import com.crawler.repository.SiteRepository;
import com.crawler.service.SiteService;
import com.crawler.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteImpl implements SiteService {

    @Autowired
    private SiteRepository repository;

    @Override
    public Site save(@GroupValid Site site) {
        EntityUtil.init(site);
        return repository.save(site);
    }

    @Override
    public void update(Site site) {
        Site oldSite = repository.findById(site.getId()).get();
        site.setVersion(oldSite.getVersion());
        repository.save(site);
    }

    @Override
    public void delete(Site site) {
        repository.delete(site);
    }

    @Override
    public List<Site> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Site> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Site findById(String id) {
        Optional<Site> siteOptional = repository.findById(id);
        return siteOptional.get();
    }
}
