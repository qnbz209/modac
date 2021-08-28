package com.modac.server.dto;

import com.modac.server.domain.TaskStatus;
import com.modac.server.domain.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TaskDetail {

    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;

    @Enumerated(value = EnumType.STRING)
    private TaskStatus status;

    private Integer continuous;

    private Integer pauseCount;

    private Long pausedTime;

    private LocalDateTime startedAt;

    private LocalDateTime pausedAt;

    private LocalDateTime lastDoneAt;

    private Long totalDuration;
}
