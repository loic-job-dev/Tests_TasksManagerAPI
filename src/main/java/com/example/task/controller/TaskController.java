package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<List<Task>> getAllTasks() {
        return ResponseEntity.ok(taskService.getTaskList());
    }

    //TODO : add tests for these routes

    @PostMapping()
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        taskService.addTask(task);
        return new ResponseEntity<>(taskService.getTaskList().getLast(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<String> deleteTask(@PathVariable(value= "taskId") String taskId) {
        try {
            taskService.deleteTask(taskId);
            return ResponseEntity.ok("Task deleted!");
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/{taskId}")
    public ResponseEntity<String> updateTask(@PathVariable(value= "taskId") String taskId, @RequestBody boolean isOver) {
        try {
            taskService.checkTask(taskId, isOver);
            return ResponseEntity.ok("Task state modified !");
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
