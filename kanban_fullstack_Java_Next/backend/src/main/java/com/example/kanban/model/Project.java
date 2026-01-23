package com.example.kanban.model;

import com.example.kanban.user.model.User;
import com.example.kanban.util.HasId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "projects")
public class Project implements HasId {
    @Id
    @Column(length = 36, name = "project_id")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(length = 1000)
    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String title;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("project")
    private List<Task> tasks = new ArrayList<>();

    @ManyToMany(mappedBy = "projects")
    @JsonIgnoreProperties("projects")
    private List<User> users = new ArrayList<>();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

}
