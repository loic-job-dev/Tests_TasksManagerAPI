package com.example.task;

import com.example.task.service.TaskService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskApplicationTests {

	// We will not write any test cases here, as we are not going to test the main method
	// Application functional tests will be written while testing the front-end with Vue.js

    @Test
    void contextLoads() { // Keep this default sanity check test case
    }

    @Test
    void testEmptyListByDefault() {
        TaskService taskService = new TaskService();

        assertTrue(taskService.getTaskList().isEmpty());
    }

    @Test
    void testAddTask(){
        TaskService taskService = new TaskService();

        Task task1 = new Task("pippo");
        taskService.addTask(task1);

        assertFalse(taskService.getTaskList().isEmpty());
        assertSame(task1, taskService.getTaskList().get(0));
    }

    @Test
    void testDeleteTask(){
        TaskService taskService = new TaskService();
        Task task1 = new Task("pippo");
        taskService.addTask(task1);
        Task task2 = new Task("foo");
        taskService.addTask(task2);

        assertEquals(2, taskService.getTaskList().size());

        taskService.deleteTask(task1.getId());

        assertEquals(1, taskService.getTaskList().size());
        assertSame(task2, taskService.getTaskList().get(0));
    }

    @Test
    void testTaskStateIsChanged() {
        TaskService taskService = new TaskService();
        Task task1 = new Task("pippo");
        taskService.addTask(task1);

        assertFalse(taskService.getTaskList().get(0).isOver());

        taskService.getTaskList().get(0).setOver(true);

        assertTrue(taskService.getTaskList().get(0).isOver());
    }
}
