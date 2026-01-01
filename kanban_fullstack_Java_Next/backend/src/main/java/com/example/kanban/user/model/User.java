package com.example.kanban.user.model;

import com.example.kanban.model.Project;
import com.example.kanban.model.Task;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter

@Entity
@Table(name = "users")
public class User {
    @Id
    @Column(length = 36, name = "user_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @OneToMany(mappedBy = "user")
    @JsonIgnoreProperties("user")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_projects",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    @JsonIgnoreProperties("users")
    private List<Project> projects = new ArrayList<>();

}
