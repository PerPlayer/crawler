package com.crawler.service;

import com.crawler.crawler.model.Element;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ElementService {

    Element save(Element element);

    void update(String id, String column, Object value);

    void delete(Element element);

    List<Element> findAll();

    Page<Element> findAll(Pageable page);

    Element findById(String id);
}
