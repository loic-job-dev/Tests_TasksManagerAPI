package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService; // real TaskService object automatically injected in TaskController instance

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void index_should_return_task_list () throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(content().json("[{\"description\":\"pippo\"},{\"description\":\"foo\"},{\"description\":\"tizzio\"}]"));
    }

    @Test
    void task_list_has_good_size() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    void task_list_not_over_by_default() throws Exception {
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].over", is(false)))
                .andExpect(jsonPath("$[1].over", is(false)))
                .andExpect(jsonPath("$[2].over", is(false)));
    }

    @Test
    void tasklist_could_have_two_tasks_with_same_description() throws Exception {
        taskService.addTask(new Task("pippo"));
        mockMvc.perform(get("/tasks"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].description", is("pippo")))
                .andExpect(jsonPath("$[3].description", is("pippo")));
        taskService.deleteTask(taskService.getTaskList().get(3).getId());
    }

    //Tests for POST route
    @Test
    void task_is_added() throws Exception {
        Task newTask = new Task("added from Post");

        mockMvc.perform(post("/tasks")
                        .content(asJsonString(newTask))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.description", is("added from Post")));

        taskService.deleteTask(taskService.getTaskList().get(3).getId());
    }

    @Test
    void wrong_task_format_cannot_be_added() throws Exception {
        mockMvc.perform(post("/tasks")
                        .content(asJsonString("newTask"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //Tests for DELETE route
    @Test
    void task_is_deleted() throws Exception {
        String taskId = taskService.getTaskList().get(2).getId();

        mockMvc.perform(delete("/tasks/{taskId}", taskId))
                .andExpect(status().isAccepted());

        taskService.addTask(new Task("tizzio"));
    }

    @Test
    void task_is_deleted_with_error() throws Exception {

        mockMvc.perform(delete("/tasks/{taskId}", "wrongId"))
                .andExpect(status().isNotFound());
    }

    //Tests for PUT route
    @Test
    void task_is_updated() throws Exception {
        String taskId = taskService.getTaskList().get(0).getId();

        mockMvc.perform(put("/tasks/{taskId}", taskId)
                        .content("true")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.over").value(true));
    }

    @Test
    void task_is_updated_with_error() throws Exception {

        mockMvc.perform(put("/tasks/{taskId}", "wrongId")
                        .content("true") // body requis pour @RequestBody boolean
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
