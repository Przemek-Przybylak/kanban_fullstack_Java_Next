package com.example.kanban.service;

import com.example.kanban.DTO.*;
import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
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
public final class ProjectService implements ProjectServiceInterface {
    private final ProjectRepository projectRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final UserService userService;


    public ProjectService(final ProjectRepository projectRepository, final TaskRepository taskRepository, final UserRepository userRepository, final UserService userService) {
        this.projectRepository = projectRepository;
        this.taskRepository = taskRepository;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @Transactional
    @Override
    public List<TaskResponseDto> getTasksByProject(final String id) {
        final var taskList = taskRepository.findByProjectId(id).stream()
                .map(Mapper::toDto)
                .toList();

        return taskList;
    }

    @Transactional
    @Override
    public TaskResponseDto addTask(final String projectId, final TaskRequestDto taskDto, final String username) {
        final Project project = getProjectIfExisting(projectId);
        checkProjectMembership(username, project);

        var task = Mapper.fromDto(taskDto);

        final var user = userRepository.findByUsername(taskDto.username())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not exist"));

        task.setUser(user);
        task.setProject(project);

        taskRepository.save(task);

        return Mapper.toDto(task);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectResponseDto> getAllProjects() {
        final var projects = projectRepository.findAll();

        return projects.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectResponseDto getProject(final String id) {
        final var project = getProjectIfExisting(id);

        return Mapper.toDto(project);
    }

    @Transactional
    @Override
    public ProjectResponseDto addProject(final ProjectRequestDto projectDto, final String username) {
        var owner = getOwner(username);
        var project = Mapper.fromDto(projectDto);

        final var savedProject = projectRepository.save(project);

        project.getUsers().add(owner);
        owner.getProjects().add(project);

        checkProjectMembership(username, project);

        return Mapper.toDto(savedProject);
    }

    @Transactional
    @Override
    public ProjectResponseDto editProject(final String id, final ProjectRequestDto projectDto, final String username) {
        var existingProject = getProjectIfExisting(id);

        checkProjectMembership(username, existingProject);

        existingProject.setTitle(projectDto.title());
        existingProject.setDescription(projectDto.description());

        final var savedProject = projectRepository.save(existingProject);

        return Mapper.toDto(savedProject);
    }

    @Transactional
    @Override
    public ProjectResponseDto editPartialProject(final String id, final ProjectPatchRequestDto project, final String username) {
        Project existingProject = getProjectIfExisting(id);

        checkProjectMembership(username, existingProject);

        updateIfNotNull(project.description(), existingProject::setDescription);
        updateIfNotNull(project.title(), existingProject::setTitle);

        final var savedProject = projectRepository.save(existingProject);

        return Mapper.toDto(savedProject);
    }

    @Transactional
    @Override
    public void deleteProject(final String id, final String username) {
        var project = getProjectIfExisting(id);

        checkProjectMembership(username, project);

        if (project.getUsers() != null) {
            for (User user : new ArrayList<>(project.getUsers())) {
                user.getProjects().remove(project);
            }
            project.getUsers().clear();
        }

        projectRepository.delete(project);
    }

    private Project getProjectIfExisting(final String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Project not found"));
    }

    private User getOwner(final String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
    }

    private void checkProjectMembership(final String username, final Project project) {
        String ownerId = userService.getUserIdFromUsername(username);

        boolean isMember = project.getUsers().stream()
                .anyMatch(user -> Objects.equals(user.getId(), ownerId));

        if (!isMember) {
            throw new AccessDeniedException("You don't have access for this project");
        }
    }
}

