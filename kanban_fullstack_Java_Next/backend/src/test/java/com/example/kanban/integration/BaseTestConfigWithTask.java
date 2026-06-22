package com.example.kanban.integration;

import com.example.kanban.model.Project;
import com.example.kanban.model.Task;
import org.junit.jupiter.api.BeforeEach;

public class BaseTestConfigWithTask extends  BaseTestConfig{

    protected Task task = new Task();
    protected Project project = new Project();

    @BeforeEach
    public void setup(){
        project.setTitle("Test project title");
        project.setDescription("Test project description");

        task.setUser(user);
        task.setTitle("Test task title");
        task.setDescription("Test task description");
        task.setStatus("todo");
        task.setProject(project);
    }
}
