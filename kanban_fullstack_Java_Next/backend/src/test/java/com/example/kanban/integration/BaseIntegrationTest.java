package com.example.kanban.integration;

import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.user.model.Role;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

@SpringBootTest
@AutoConfigureMockMvc
@Testcontainers
@ActiveProfiles("test")
@TestPropertySource(locations = "classpath:application.properties")
@Transactional
public abstract class BaseIntegrationTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    protected String projectId;
    protected Project savedProject;
    protected User user = new User();

    @Container
    static PostgreSQLContainer<?> postgres = new PostgreSQLContainer<>("postgres:17-alpine");

    @BeforeEach
    void setUp() {
        user.setUsername("przemek");
        user.setPassword("password");
        user.setRole(Role.ADMIN);
        userRepository.save(user);

        Project project = new Project();
        project.setTitle("Test Project");

        project.getUsers().add(user);

        savedProject = projectRepository.save(project);
        projectId = savedProject.getId();
    }

    @DynamicPropertySource
    static void configureProperties(DynamicPropertyRegistry registry) {

        registry.add("spring.datasource.url", postgres::getJdbcUrl);
        registry.add("spring.datasource.username", postgres::getUsername);
        registry.add("spring.datasource.password", postgres::getPassword);

        registry.add("app.jwt.secret", () -> "9zXvR2wK4mP7qT9sB2vK4mP7qT9sB2vK4mP7qT9sB2vK4mP7qT9sB2vK4mP7qT9s");
    }
}