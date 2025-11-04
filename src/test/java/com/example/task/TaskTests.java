package com.example.task;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TaskTests {

    // We will not write any test cases here, as we are not going to test the main method
    // Application functional tests will be written while testing the front-end with Vue.js

    @Test
    void testTaskHasAUniqueId() {
        Task task1 = new Task("pippo");
        Task task2 = new Task("pippo");

        assertNotSame(task1.getId(), task2.getId());
    }

    @Test
    void testTaskIsNotOverByDefault() {
        Task task1 = new Task("pippo");

        assertFalse(task1.isOver());
    }

    @Test
    void testTaskCanBeOver() {
        Task task1 = new Task("pippo");

        task1.setOver(true);

        assertTrue(task1.isOver());
    }
}
