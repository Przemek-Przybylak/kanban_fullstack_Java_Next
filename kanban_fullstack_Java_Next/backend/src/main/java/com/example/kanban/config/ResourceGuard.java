package com.example.kanban.config;

import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component("guard")
public class ResourceGuard {

    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;

    public ResourceGuard(ProjectRepository projectRepository, TaskRepository taskRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
    }

    public boolean canAccessProject(String projectId) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();

        return projectRepository.findById(projectId)
                .map(p -> p.getUsers().stream()
                        .anyMatch(u -> u.getUsername().equals(user)))
                .orElse(false);
    }

    public boolean canAccessTask(String taskId) {
        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        return taskRepository.findById(taskId)
                .map(t -> t.getUser().getUsername().equals(user))
                .orElse(false);
    }
}