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

    @PostMapping()
    public ResponseEntity<Task> addTask(@RequestBody Task task) {
        taskService.addTask(task);
        return new ResponseEntity<>(taskService.getTaskList().getLast(), HttpStatus.CREATED);
    }

    @DeleteMapping("/{taskId}")
    public ResponseEntity<HttpStatus> deleteTask(@PathVariable("taskId") String taskId) {
        try {
            taskService.deleteTask(taskId);
            return new ResponseEntity<HttpStatus>(HttpStatus.ACCEPTED);
        } catch (UnsupportedOperationException e) {
            return new ResponseEntity<HttpStatus>(HttpStatus.NOT_FOUND);
        }
    }

    //TODO : add tests for this route

    @PutMapping("/{taskId}")
    public ResponseEntity<Task> updateTask(@PathVariable("taskId") String taskId, @RequestBody boolean isOver) {
        try {
            taskService.checkTask(taskId, isOver);
            Task updatedTask = taskService.getTaskById(taskId);
            return ResponseEntity.ok(updatedTask);
        } catch (UnsupportedOperationException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
