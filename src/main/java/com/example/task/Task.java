package com.example.task;

import java.util.UUID;

public class Task {
    private String id;
    private String description;
    private boolean isOver;

    public Task (String description) {
        this.id = UUID.randomUUID().toString();
        this.description = description;
        this.isOver = false;
    }

    public String getId() {
        return this.id;
    }

    public String getDescription() {
        return this.description;
    }

    public boolean isOver() {
        return this.isOver;
    }
    public void setOver(boolean over) {
        this.isOver = over;
    }
}
