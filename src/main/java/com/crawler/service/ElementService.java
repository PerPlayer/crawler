package com.crawler.service;

import com.crawler.crawler.model.Element;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ElementService {

    Element save(Element element);

    void delete(long id);

    List<Element> findAll();

    Page<Element> findAll(Pageable page);

    Element findById(long id);
}
