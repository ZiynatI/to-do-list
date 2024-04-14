package org.zi.dao;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.zi.entity.Task;

public interface TaskDao extends CrudRepository<Task, Long> {
    List<Task> findByName(String name);

    Task findById(long id);
}
