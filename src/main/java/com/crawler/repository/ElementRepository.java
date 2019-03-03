package com.crawler.repository;

import com.crawler.crawler.model.Element;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.Repository;

public interface ElementRepository extends JpaRepository<Element, String> {
}
