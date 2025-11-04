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

    public void deleteTask (String id) throws UnsupportedOperationException {
        boolean removed = taskList.removeIf(task -> task.getId().equals(id));
        if (!removed) {
            throw new UnsupportedOperationException("Task not found");
        }
    }

    public List<Task> getTaskList() {
        return this.taskList;
    }

    public Task getTaskById(String id) throws UnsupportedOperationException {
        for (Task task : taskList) {
            if (task.getId().equals(id)) {
                return task;
            }
        }
        throw new UnsupportedOperationException("Task not found");
    }

    public void checkTask(String id, boolean isOver) throws UnsupportedOperationException {
        boolean changed = false;
        for (Task task : taskList) {
            if (task.getId().equals(id)) {
                task.setOver(isOver);
                changed = true;
            }
        }
        if (!changed) {
            throw new UnsupportedOperationException("Task not found");
        }
    }
}
