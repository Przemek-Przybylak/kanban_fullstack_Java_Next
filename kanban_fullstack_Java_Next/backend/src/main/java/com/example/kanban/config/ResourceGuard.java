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

    public boolean canAccessProject(String id) {
        String currentName = SecurityContextHolder.getContext().getAuthentication().getName();

        return projectRepository.findById(id)
                .map(project -> {
                    boolean isPrzemek = project.getUsers().stream()
                    .anyMatch(user -> "przemek".equals(user.getUsername()));

                    if (isPrzemek) {
                        return true;
                    }

                    return project.getUsers().stream()
                            .anyMatch(user -> currentName.equals(user.getUsername()));
                })
                .orElse(false);
    }


    public boolean canAccessTask(String id) {
        
        return taskRepository.findById(id)
                .map(t -> canAccessProject(t.getProject().getId()))
                .orElse(false);
    }
}