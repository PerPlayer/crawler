package com.crawler.service.impl;

import com.crawler.crawler.model.Element;
import com.crawler.repository.ElementRepository;
import com.crawler.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ElementImpl implements ElementService {

    @Autowired
    private ElementRepository repository;

    @Override
    public Element save(Element element) {
        return repository.save(element);
    }

    @Override
    public void delete(long id) {
        repository.delete(id);
    }

    @Override
    public List<Element> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Element> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Element findById(long id) {
        return repository.findOne(id);
    }
}
