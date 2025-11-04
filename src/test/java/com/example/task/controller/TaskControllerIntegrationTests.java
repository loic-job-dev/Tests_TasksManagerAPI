package com.example.task.controller;

import com.example.task.model.Task;
import com.example.task.service.TaskService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class TaskControllerIntegrationTests {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TaskService taskService; // real TaskService object automatically injected in TaskController instance


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
}
