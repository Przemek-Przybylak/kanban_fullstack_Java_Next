package com.example.kanban.integration;

import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseTestConfigWithTask extends  BaseTestConfig{

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private TaskRepository taskRepository;

    protected Task task = new Task();
    protected Project project = new Project();
    protected String taskId = task.getId();

    @BeforeEach
    public void setup(){
        project.setTitle("Test project title");
        project.setDescription("Test project description");

        project = projectRepository.save(project);

        task.setUser(user);
        task.setTitle("Test task title");
        task.setDescription("Test task description");
        task.setStatus("todo");
        task.setProject(project);

        task = taskRepository.save(task);

        this.taskId = task.getId();
        this.projectId = project.getId();
    }
}
