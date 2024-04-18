package org.zi;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.zi.dao.TaskDao;
import org.zi.entity.Task;
import org.zi.entity.TaskStatus;

import java.util.Objects;

@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TaskControllerTest {

    TestRestTemplate restTemplate = new TestRestTemplate();
    HttpHeaders headers = new HttpHeaders();
    @LocalServerPort
    private int port;

    @Autowired
    private TaskDao taskDao;

    @Test
    public void testCreateTask() {
        Task task = new Task("Test", "Test Description", TaskStatus.NEW);
        HttpEntity<Task> entity = new HttpEntity<>(task, headers);

        ResponseEntity<Task> response = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.POST,
                entity, Task.class);
        assertTrue(response.getStatusCode().is2xxSuccessful(), response.toString());

        Task responseTask = response.getBody();
        assertNotNull(responseTask);
        assertEquals(task.getName(), responseTask.getName());
        assertEquals(task.getDescription(), responseTask.getDescription());
        assertEquals(task.getStatus(), responseTask.getStatus());
    }

    @Test
    public void testGetAll() {
        Task task1 = new Task("Test1", "Test Description 1", TaskStatus.NEW);
        Task task2 = new Task("Test2", "Test Description 2", TaskStatus.NEW);
        HttpEntity<Task> entity = new HttpEntity<>(task1, headers);
        ResponseEntity<Task> response = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.POST,
                entity, Task.class);
        HttpEntity<Task> entity2 = new HttpEntity<>(task2, headers);
        ResponseEntity<Task> response2 = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.POST,
                entity2, Task.class);
        HttpEntity<Task> entity3 = new HttpEntity<>(null, headers);
        ResponseEntity<Task[]> response3 = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.GET,
                entity3, (Task[].class));
        assertNotNull(response3);
        assertEquals(2, Objects.requireNonNull(response3.getBody()).length);
        assertEquals(task1.getName(), response3.getBody()[0].getName());
        assertEquals(task2.getName(), response3.getBody()[1].getName());
        assertEquals(task1.getDescription(), response3.getBody()[0].getDescription());
    }

    @Test
    public void testGetById() {
        Task task1 = new Task("Test", "Test Description", TaskStatus.NEW);
        HttpEntity<Task> entity = new HttpEntity<>(task1, headers);
        ResponseEntity<Task> response = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.POST,
                entity, Task.class);
        HttpEntity<Task> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<Task> response2 = restTemplate.exchange(makeLocalUrl("tasks/1"), HttpMethod.GET,
                entity2, Task.class);
        assertNotNull(response2.getBody());
        assertEquals(task1.getName(), response2.getBody().getName());
    }


    @Test
    public void testUpdate() {
        Task task1 = new Task("Test", "Test Description", TaskStatus.NEW);
        HttpEntity<Task> entity = new HttpEntity<>(task1, headers);
        ResponseEntity<Task> response = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.POST,
                entity, Task.class);
        Task task2 = new Task("NewTest", "NewDescription", TaskStatus.DONE);
        HttpEntity<Task> entity2 = new HttpEntity<>(task2, headers);
        ResponseEntity<Task> response2 = restTemplate.exchange(makeLocalUrl("tasks/1"), HttpMethod.PUT,
                entity2, Task.class);
        assertNotNull(response2);
        assertEquals(task2.getName(), response2.getBody().getName());
        assertEquals(task2.getDescription(), response2.getBody().getDescription());
        assertEquals(task2.getStatus(), response2.getBody().getStatus());
    }


    @Test
    public void testDelete() {
        Task task1 = new Task("Test", "Test Description", TaskStatus.NEW);
        HttpEntity<Task> entity = new HttpEntity<>(task1, headers);
        ResponseEntity<Task> response = restTemplate.exchange(makeLocalUrl("tasks"), HttpMethod.POST,
                entity, Task.class);
        HttpEntity<Task> entity2 = new HttpEntity<>(null, headers);
        ResponseEntity<Task> response2 = restTemplate.exchange(makeLocalUrl("tasks/1"), HttpMethod.DELETE,
                entity2, Task.class);
        HttpEntity<Task> entity3 = new HttpEntity<>(null, headers);
        ResponseEntity<Task> response3 = restTemplate.exchange(makeLocalUrl("tasks/1"), HttpMethod.GET,
                entity3, Task.class);
        assertNull(response3.getBody());
    }

    private String makeLocalUrl(String uri) {
        return "http://localhost:" + port + "/" + uri;
    }
}
