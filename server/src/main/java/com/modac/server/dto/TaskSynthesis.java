package com.modac.server.dto;

import com.modac.server.domain.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class TaskSynthesis {

    private Long userId;

    private Map<TaskType, Long> taskTypeDurationMap;

    private Map<Long, Long> mostDoneTypeDurationMap;
}
