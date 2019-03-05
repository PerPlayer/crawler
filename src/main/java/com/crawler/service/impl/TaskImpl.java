package com.crawler.service.impl;

import com.crawler.annonation.GroupValid;
import com.crawler.crawler.model.Task;
import com.crawler.dao.TaskDao;
import com.crawler.repository.TaskRepository;
import com.crawler.service.TaskService;
import com.crawler.util.EntityUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TaskImpl implements TaskService {

    @Autowired
    private TaskRepository repository;

    @Autowired
    private TaskDao dao;

    @Override
    public Task save(@GroupValid Task task) {
        EntityUtil.init(task);
        task.setWeight(10);
        return repository.save(task);
    }

    @Override
    public void update(String id, String column, Object value) {
        dao.update(id, column, value);
    }

    @Override
    public void update(Task task) {
        Task oldTask = repository.findById(task.getId()).get();
        task.setVersion(oldTask.getVersion());
        repository.save(task);
    }

    @Override
    public void delete(Task task) {
        repository.delete(task);
    }

    @Override
    public List<Task> findAll() {
        return repository.findAll();
    }

    @Override
    public Page<Task> findAll(Pageable page) {
        return repository.findAll(page);
    }

    @Override
    public Task findById(String id) {
        Optional<Task> taskOptional = repository.findById(id);
        return taskOptional.get();
    }

    @Override
    public List<Task> findByStatusAndDeepLessThan(int status, int deep) {
        return repository.findByStatusAndDeepLessThan(status, deep);
    }
}
