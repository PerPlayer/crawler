package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.Element;
import com.crawler.dao.ElementDao;
import com.crawler.repository.ElementRepository;
import com.crawler.service.ElementService;
import com.crawler.util.IdGeneratorUtil;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Date;
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
        element.setId(IdGeneratorUtil.id());
        Date time = DateTime.now().toDate();
        element.setWeight(10);
        element.setStatus(1);
        element.setCreateTime(time);
        element.setModifyTime(time);
        return repository.save(element);
    }

    @Override
    public void update(String id, String column, Object value) {
        dao.update(id, column, value);
    }

    @Override
    public void update(Element element) {
        Element elem = repository.findById(element.getId()).get();
        element.setVersion(elem.getVersion());
        repository.save(element);
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
