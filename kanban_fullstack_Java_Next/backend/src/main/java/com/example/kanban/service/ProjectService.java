package com.example.kanban.service;

import com.example.kanban.DTO.Mapper;
import com.example.kanban.DTO.ProjectPatchRequestDto;
import com.example.kanban.DTO.ProjectRequestDto;
import com.example.kanban.DTO.ProjectResponseDto;
import com.example.kanban.exception.ForbiddenException;
import com.example.kanban.exception.NotFoundException;
import com.example.kanban.model.Project;
import com.example.kanban.model.ProjectRepository;
import com.example.kanban.model.TaskRepository;
import com.example.kanban.user.repository.UserRepository;
import com.example.kanban.user.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

import static com.example.kanban.util.UpdateIfNotNull.updateIfNotNull;

@Service
@Transactional(readOnly = true)
public class ProjectService implements ProjectServiceInterface {
    private final ProjectRepository projectRepository;
    private final UserService userService;


    public ProjectService(final ProjectRepository projectRepository, final TaskRepository taskRepository, final UserRepository userRepository, final UserService userService) {
        this.projectRepository = projectRepository;
        this.userService = userService;
    }

    @Override
    public List<ProjectResponseDto> getAllProjects() {
        final var projects = projectRepository.findAll();

        return projects.stream()
                .map(Mapper::toDto)
                .toList();
    }

    @Override
    public ProjectResponseDto getProject(final String id) {
        final var project = getProjectIfExisting(id);

        return Mapper.toDto(project);
    }

    @Override
    @Transactional
    public ProjectResponseDto addProject(final ProjectRequestDto projectDto, final String username) {
        var owner = userService.getOwner(username);
        var project = Mapper.fromDto(projectDto);

        project.getUsers().add(owner);
        owner.getProjects().add(project);

        final var savedProject = projectRepository.save(project);

        return Mapper.toDto(savedProject);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    public ProjectResponseDto editProject(final String id, final ProjectRequestDto projectDto, final String username) {
        var existingProject = getProjectIfExisting(id);

        existingProject.setTitle(projectDto.title());
        existingProject.setDescription(projectDto.description());

        final var savedProject = projectRepository.save(existingProject);

        return Mapper.toDto(savedProject);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    public ProjectResponseDto editPartialProject(final String id, final ProjectPatchRequestDto project, final String username) {
        var existingProject = getProjectIfExisting(id);

        updateIfNotNull(project.description(), existingProject::setDescription);
        updateIfNotNull(project.title(), existingProject::setTitle);

        final var savedProject = projectRepository.save(existingProject);

        return Mapper.toDto(savedProject);
    }

    @Override
    @Transactional
    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('ROLE_ADMIN') or @guard.canAccessProject(#id)")
    public void deleteProject(final String id) {
        final var project = getProjectIfExisting(id);

        if (project.getUsers() != null) {
            project.getUsers().forEach(user -> user.getProjects().remove(project));
            project.getUsers().clear();
        }

        projectRepository.delete(project);
    }

    public Project getProjectIfExisting(final String id) {
        return projectRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("project", "id: " + id));
    }

    private void checkProjectMembership(final String username, final Project project) {
        final var ownerId = userService.getUserIdFromUsername(username);

        boolean isMember = project.getUsers().stream()
                .anyMatch(user -> Objects.equals(user.getId(), ownerId));

        if (!isMember) {
            throw new ForbiddenException("project");
        }
    }
}

