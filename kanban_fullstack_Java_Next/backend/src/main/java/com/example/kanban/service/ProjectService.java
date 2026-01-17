package com.example.kanban.service;

import com.example.kanban.DTO.*;
import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.Task;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.model.User;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.example.kanban.util.UpdateIfNotNull.updateIfNotNull;

@Service
public class ProjectService implements ProjectServiceInterface {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public ProjectService(ProjectRepository projectRepository, TaskRepository taskRepository, UserRepository userRepository, UserService userService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public List<TaskResponseDto> getTasksByProject(String id) {
        return taskRepository.findByProjectId(id).stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Transactional
    @Override
    public TaskResponseDto addTask(String projectId, TaskRequestDto taskDto, String username) {
        Project project = getProjectIfExisting(projectId);
        checkProjectMembership(username, project);

        Task task = Mapper.fromDto(taskDto);

        User user = userRepository.findByUsername(taskDto.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));

        task.setUser(user);
        task.setProject(project);

        taskRepository.save(task);

        return Mapper.toDto(task);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponseDto> getAllProjects() {
        List<Project> projects = projectRepository.findAll();

        return projects.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectResponseDto getProject(String id) {
        Project project = getProjectIfExisting(id);

        return Mapper.toDto(project);
    }

    @Transactional
    @Override
    public ProjectResponseDto addProject(ProjectRequestDto projectDto, String username) {
        User owner = getOwner(username);
        Project project = Mapper.fromDto(projectDto);

        Project savedProject = projectRepository.save(project);

        project.getUsers().add(owner);
        owner.getProjects().add(project);

        checkProjectMembership(username, project);

        return Mapper.toDto(savedProject);
    }

    @Transactional
    @Override
    public ProjectResponseDto editProject(String id, ProjectRequestDto projectDto, String username) {
        Project existingProject = getProjectIfExisting(id);

        checkProjectMembership(username, existingProject);

        existingProject.setTitle(projectDto.title());
        existingProject.setDescription(projectDto.description());

        Project savedProject = projectRepository.save(existingProject);

        return Mapper.toDto(savedProject);
    }

    @Transactional
    @Override
    public ProjectResponseDto editPartialProject(String id, ProjectPatchRequestDto project, String username) {
        Project existingProject = getProjectIfExisting(id);

        checkProjectMembership(username, existingProject);

        updateIfNotNull(project.description(), existingProject::setDescription);
        updateIfNotNull(project.title(), existingProject::setTitle);

        Project savedProject = projectRepository.save(existingProject);

        return Mapper.toDto(savedProject);
    }

    @Transactional
    @Override
    public void deleteProject(String id, String username) {
        Project project = getProjectIfExisting(id);

        checkProjectMembership(username, project);

        if (project.getUsers() != null) {
            for (User user : new ArrayList<>(project.getUsers())) {
                user.getProjects().remove(project);
            }
            project.getUsers().clear();
        }

        projectRepository.delete(project);
    }

    private Project getProjectIfExisting(String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private User getOwner(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void checkProjectMembership(String username, Project project) {
        String ownerId = userService.getUserIdFromUsername(username);

        boolean isMember = project.getUsers().stream()
                .anyMatch(user -> Objects.equals(user.getId(), ownerId));

        if (!isMember) {
            throw new AccessDeniedException("You don't have access for this project");
        }
    }
}

