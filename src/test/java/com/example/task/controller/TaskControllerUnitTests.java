package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;


import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TaskController.class)
public class TaskControllerUnitTests {
    @Autowired
    private MockMvc mockMvc; //Object used to make HTTP request on our API

    @MockitoBean
    private TaskService taskService; //Mock object TaskService automatically injected in TaskController instance

    private List<Task> mockTasks;

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    void setUp() {
        Task task1 = new Task("pippo");
        Task task2 = new Task("foo");
        mockTasks = new ArrayList<>();
        mockTasks.add(task1);
        mockTasks.add(task2);
    }

    @Test
    void hello_should_return_message() throws Exception {
        mockMvc.perform(get("/tasks/hello"))
                .andExpect(status().isOk())
                .andExpect(content().string("Welcome to the Task Manager API!"));
    }

    @Test
    void index_should_return_task_list() throws Exception {
        when(taskService.getTaskList()).thenReturn(mockTasks);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"description\":\"pippo\"},{\"description\":\"foo\"}]"));
    }

    //Tests pour GET route
    @Test
    void task_list_has_good_size() throws Exception {
        when(taskService.getTaskList()).thenReturn(mockTasks);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void task_list_not_over_by_default() throws Exception {
        when(taskService.getTaskList()).thenReturn(mockTasks);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].over", is(false)))
                .andExpect(jsonPath("$[1].over", is(false)));
    }

    @Test
    void tasklist_could_have_two_tasks_with_same_description() throws Exception {
        mockTasks.add(new Task("pippo"));
        when(taskService.getTaskList()).thenReturn(mockTasks);
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is("pippo")))
                .andExpect(jsonPath("$[2].description", is("pippo")));
    }

    //Tests for POST route
    @Test
    void task_is_added() throws Exception {
        Task newTask = new Task("added from Post");
        mockTasks.add(newTask);
        when(taskService.getTaskList()).thenReturn(mockTasks);
        mockMvc.perform(post("/tasks")
                .content(asJsonString(newTask))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("added from Post")));
        mockTasks.remove(newTask);
    }

    @Test
    void wrong_task_format_cannot_be_added() throws Exception {
        when(taskService.getTaskList()).thenReturn(mockTasks);
        mockMvc.perform(post("/tasks")
                        .content(asJsonString("newTask"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //Tests for DELETE route
    @Test
    void task_is_deleted() throws Exception {
        String taskId = mockTasks.get(1).getId();
        when(taskService.getTaskList()).thenReturn(mockTasks);

        mockMvc.perform(delete("/tasks/{taskId}", taskId))
                .andExpect(status().isAccepted());
    }

    @Test
    void task_is_deleted_with_error() throws Exception {
        doThrow(new UnsupportedOperationException()).when(taskService).deleteTask("wrongId");

        mockMvc.perform(delete("/tasks/{taskId}", "wrongId"))
                .andExpect(status().isNotFound());
    }

    //Tests for PUT route
    @Test
    void task_is_updated() throws Exception {
        String taskId = mockTasks.get(0).getId();
        Task task = mockTasks.get(0);

        doAnswer(invocation -> {
            task.setOver(true);
            return null;
        }).when(taskService).checkTask(taskId, true);

        when(taskService.getTaskById(taskId)).thenReturn(task);

        mockMvc.perform(put("/tasks/{taskId}", taskId)
                .content("true")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.over").value(true));
    }

    @Test
    void task_is_updated_with_error() throws Exception {
        String wrongId = "wrongId";

        doThrow(new UnsupportedOperationException("Task not found"))
                .when(taskService).checkTask(eq(wrongId), eq(true));

        mockMvc.perform(put("/tasks/{taskId}", wrongId)
                        .content("true") // body requis pour @RequestBody boolean
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
