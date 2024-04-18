package org.zi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.zi.dao.TaskDao;
import org.zi.entity.Task;

import java.util.List;
import java.util.Optional;

@RestController
public class TaskController {

    @Autowired
    TaskDao taskDao;

    @PostMapping("tasks")
    Task createTask(@RequestBody Task task) {
        taskDao.save(task);
        return task;
    }

    @GetMapping("tasks")
    List<Task> getAll() {
        return taskDao.findAll();
    }

    @GetMapping("tasks/{id}")
    Optional<Task> getById(@PathVariable Long id) {
        return taskDao.findById(id);
    }

    @PutMapping("tasks/{id}")
    Task update(@PathVariable Long id, @RequestBody Task task) {
        task.setId(id);
        taskDao.save(task);
        return task;
    }

    @DeleteMapping("tasks/{id}")
    void delete(@PathVariable Long id) {
        taskDao.deleteById(id);
    }
}