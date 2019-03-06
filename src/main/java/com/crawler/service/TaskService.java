package com.crawler.service;

import com.crawler.crawler.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TaskService {

    Task save(Task task);

    void update(String id, String column, Object value);

    void update(Task task);

    void delete(Task task);

    List<Task> findAll();

    Page<Task> findAll(Pageable page);

    Task findById(String id);

    List<Task> findByStatusAndDeepLessThan(int status, int deep);

    long countByHref(String href);
}
