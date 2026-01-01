package com.example.kanban.model;

import com.example.kanban.user.model.User;
import com.example.kanban.util.HasId;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor

@Entity
@Table(name = "tasks")
public class Task implements HasId {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(length = 36, name = "task_id")
    private String id;

    private String title;

    @Column(length = 1000)
    private String description;

    private LocalDateTime dueDate;
    private LocalDateTime createdAt;
    private String status;

    private String approvedBy;

    @ManyToOne
    @JoinColumn(name = "project_id")
    @JsonIgnoreProperties("tasks")
    private Project project = new Project();

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnoreProperties("tasks")
    private User user = new User();

    @PrePersist
    public void onCreate() {
        this.createdAt = LocalDateTime.now();
        if (this.status == null) {
            this.status = "todo";
        }
    }
}
