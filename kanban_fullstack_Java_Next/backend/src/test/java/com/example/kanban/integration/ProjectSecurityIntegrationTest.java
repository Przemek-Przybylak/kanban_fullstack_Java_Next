package com.example.kanban.integration;

import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class ProjectSecurityIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    private String projectId;

    @BeforeEach
    void setUp() {
        User user = new User();
        user.setUsername("przemek");
        user.setPassword("password");
        userRepository.save(user);

        Project project = new Project();
        project.setTitle("Test Project");

        project.getUsers().add(user);

        Project savedProject = projectRepository.save(project);
        projectId = savedProject.getId();
    }

    @Test
    void contextLoads() {
    }

    @Test
    @WithMockUser(username = "przemek")
    public void shouldAllowAccessForPrzemek() throws Exception {
        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nonPrzemek")
    public void shouldBlockAccessForNonPrzemek() throws Exception {
        mockMvc.perform(delete("/projects/" + projectId))
                .andExpect(status().isForbidden());
    }
}