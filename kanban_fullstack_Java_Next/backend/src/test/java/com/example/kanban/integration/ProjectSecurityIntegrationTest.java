package com.example.kanban.integration;

import com.example.kanban.model.Project;
import com.example.kanban.user.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.user.repository.UserRepository;;;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class ProjectSecurityIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private ProjectRepository projectRepository;

    @MockitoBean
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
    @WithMockUser(username = "przemek")
    public void shouldAllowAccessForPrzemek() throws Exception {
        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "nonPrzemek")
    public void shouldBlockAccessForNonPrzemek() throws Exception {
        mockMvc.perform(get("/projects/" + projectId))
                .andExpect(status().isForbidden());
    }
}