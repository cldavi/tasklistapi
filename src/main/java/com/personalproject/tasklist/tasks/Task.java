package com.personalproject.tasklist.tasks;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "tasks")
@Data
public class Task {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Integer id;

    @Column(length = 40)
    private String title;

    private String description;
    private LocalDateTime startedAt;
    private LocalDateTime endedAt;
    
    @Enumerated(value = EnumType.STRING)
    private TaskPriority priority;
    
    @CreationTimestamp
    private LocalDateTime createdAt;

    private UUID userId;
}
