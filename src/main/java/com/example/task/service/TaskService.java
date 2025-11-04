package com.example.task.service;

import com.example.task.model.Task;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {
    /// TO BE IMPLEMENTED
    /// Handle and manage a list of Task objects
    private List<Task> taskList = new ArrayList<>();

    public void addTask (Task task) {
        taskList.add(task);
    }

    public void deleteTask (String id) {
        taskList.removeIf(task -> task.getId().equals(id));
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public void checkTask(String id) {
        for (Task task : taskList) {
            if (task.getId().equals(id)) {
                task.setOver(true);
            }
        }
    }
}
