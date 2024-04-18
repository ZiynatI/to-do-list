package org.zi.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.zi.entity.Task;

public interface TaskDao extends JpaRepository<Task, Long> {
}
