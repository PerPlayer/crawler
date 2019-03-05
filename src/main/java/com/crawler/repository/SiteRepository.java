package com.crawler.repository;

import com.crawler.crawler.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SiteRepository extends JpaRepository<Site, String> {
}
