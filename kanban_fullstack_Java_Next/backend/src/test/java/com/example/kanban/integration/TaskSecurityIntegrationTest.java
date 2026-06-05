package com.example.kanban.integration;

import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TaskSecurityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    protected TaskRepository taskRepository;

    protected Task savedTask;

    @BeforeEach
    void setTask() {
        Task task = new Task();
        task.setTitle("First test task");
        task.setProject(savedProject);
        task.setDescription("Example description");
        task.setStatus("todo");
        task.setUser(user);

        savedTask = taskRepository.save(task);
    }

    @Test
    @WithMockUser(username = "przemek")
    public void shouldReturnTasks() throws Exception {
        mockMvc.perform(get("/projects/" + projectId + "/tasks"))
                .andExpect(status().isOk());
    }
}
