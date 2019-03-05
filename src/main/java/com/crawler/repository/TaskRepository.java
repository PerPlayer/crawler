package com.crawler.repository;

import com.crawler.crawler.model.Task;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, String> {

    List<Task> findByStatusAndDeepLessThan(int status, int deep);
}
