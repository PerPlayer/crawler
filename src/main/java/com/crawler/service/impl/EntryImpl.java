package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.Entry;
import com.crawler.crawler.model.Task;
import com.crawler.dao.EntryDao;
import com.crawler.repository.EntryRepository;
import com.crawler.service.EntryService;
import com.crawler.service.TaskService;
import com.crawler.util.EntityUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntryImpl implements EntryService {

    @Autowired
    private EntryRepository repository;

    @Autowired
    private EntryDao dao;

    @Autowired
    private TaskService taskService;

    @Override
    public Entry save(@GroupValid Entry entry) {
        EntityUtil.init(entry);
        entry.setWeight(10);
        return repository.save(entry);
    }

    @Override
    public void update(String id, String column, Object value) {
        dao.update(id, column, value);
    }

    @Override
    public void update(Entry entry) {
        Entry oldEntry = repository.findById(entry.getId()).get();
        entry.setVersion(oldEntry.getVersion());
        repository.save(entry);
    }

    @Override
    public void delete(Entry element) {
        repository.delete(element);
    }

    @Override
    public List<Entry> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Entry> findAll(Pageable page) {
        Page<Entry> entryPage = repository.findAll(page);
        setTask(entryPage);
        return entryPage;
    }


    @Override
    public Page<Entry> findAllByContent(String content, Pageable pageable) {
        Page<Entry> entryPage = null;
        if (StringUtils.isNotBlank(content)) {
            entryPage = repository.findAllByContentLike("%"+content+"%", pageable);
        }else{
            entryPage = repository.findAll(pageable);
        }
        setTask(entryPage);
        return entryPage;
    }

    @Override
    public Entry findById(String id) {
        Optional<Entry> elementOptional = repository.findById(id);
        return elementOptional.get();
    }

    private void setTask(Page<Entry> entryPage) {
        entryPage.forEach(entry -> {
            if (entry.getContent().length() > 200) {
                entry.setContent(entry.getContent().substring(0, 200));
            }
            Task task = taskService.findById(entry.getTaskId());
            entry.setTask(task);
        });
    }
}
