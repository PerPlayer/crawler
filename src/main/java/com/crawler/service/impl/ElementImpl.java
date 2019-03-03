package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.Element;
import com.crawler.dao.ElementDao;
import com.crawler.repository.ElementRepository;
import com.crawler.service.ElementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ElementImpl implements ElementService {

    @Autowired
    private ElementRepository repository;

    @Autowired
    private ElementDao dao;

    @Override
    public Element save(@GroupValid Element element) {
        return repository.save(element);
    }

    @Override
    public void update(String id, String column, Object value) {
        dao.update(id, column, value);
    }

    @Override
    public void delete(Element element) {
        repository.delete(element);
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
    public Element findById(String id) {
        Optional<Element> elementOptional = repository.findById(id);
        return elementOptional.get();
    }
}
