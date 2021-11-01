package com.modac.server.domain.entity;

import com.modac.server.domain.TaskState;
import com.modac.server.domain.TaskStatus;
import com.modac.server.domain.TaskType;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class Task {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    @Enumerated(value = EnumType.STRING)
    private TaskState state;

    private Integer continuous;

    private Integer pauseCount;

    private Long pausedTime;

    private LocalDateTime startedAt;

    private LocalDateTime pausedAt;

    private LocalDateTime lastDoneAt;

    private Long totalDuration;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @ManyToOne
    private User user;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "task")
    private List<Record> records;
}
