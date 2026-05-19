package com.example.kanban.integration;

import com.example.kanban.model.Project;
import com.example.kanban.user.model.User;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.user.repository.UserRepository;;;

@SpringBootTest
@AutoConfigureMockMvc
@TestPropertySource(properties = {
        "spring.datasource.url=jdbc:postgresql://ep-patient-tooth-ag1qmdkz-pooler.c-2.eu-central-1.aws.neon.tech/neondb?sslmode=require",
        "spring.datasource.username=neondb_owner",
        "spring.datasource.password=npg_pJZQeK5LbY1U",
        "spring.jpa.database-platform=org.hibernate.dialect.H2Dialect",
        "spring.jpa.hibernate.ddl-auto=update"
})
@Transactional
public class ProjectSecurityIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;

    @Autowired
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
        mockMvc.perform(delete("/projects/" + projectId))
                .andExpect(status().isForbidden());
    }
}