package com.desafio_hit_todo_list.hit_todolist.task.entity;

import java.time.LocalDateTime;

import org.hibernate.validator.constraints.Length;

import com.desafio_hit_todo_list.hit_todolist.task.enums.TaskStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "task")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Task {
    
    @Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Title is mandatory")
    @Length(min = 3, message = "The title must have at least 3 characters.")
    private String title;

    @NotBlank(message = "Title is mandatory")
    @Length(min = 5, message = "The description must have at least 5 characters.")
    private String description;
    
    @NotNull(message = "Status is mandatory")
    @Enumerated(EnumType.STRING)
    private TaskStatus status;

    @Min(value = 1, message = "Priority should not be less than 1")
    @Max(value = 5, message = "Priority should not be more than 5")
    private Long priority;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }


}
