package org.zi;

import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.web.bind.annotation.*;
import org.zi.dao.TaskDao;
import org.zi.entity.Task;

import java.util.ArrayList;
import java.util.List;

@RestController
public class TaskController {
    @Autowired
    NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    TaskDao taskDao;

    @GetMapping("tasks")
    List<Task> getAllTasks() {
        var result = new ArrayList<Task>();
        for (var task : taskDao.findAll()) {
            result.add(task);
        }
        return result;
    }

    @DeleteMapping ("tasks/{id}")
    void deleteTaskById(@PathVariable Long id) {
        taskDao.deleteById(id);
    }

    @RequestMapping("/")
    String hello() {
        Task task = new Task("newTask", "desc", "new");
        taskDao.save(task);
        System.out.println("List: " + taskDao.findByName("newTask"));
        return "Hello World!";
    }
}
