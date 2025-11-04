package com.example.task.controller;

import com.example.task.Task;
import com.example.task.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController<T> {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;

        //Add some random tasks to easy tests
        taskService.addTask(new Task("pippo"));
        taskService.addTask(new Task("foo"));
        taskService.addTask(new Task("tizzio"));
    }

    @GetMapping("/hello")
    public ResponseEntity<String> hello() {
        return ResponseEntity.ok("Welcome to the Task Manager API!");
    }

    @GetMapping()
    public ResponseEntity<List<T>> getAllTasks() { return ResponseEntity.ok((List<T>) taskService.getTaskList());}
}
