package com.modac.server.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@Data
@RequiredArgsConstructor
public class TaskRoom {

    private final HashMap<Long, Long> members;
    private final TaskType type;

    public TaskRoom(TaskType type) {

        this.type = type;
        this.members = new HashMap<>();
    }

    public TaskRoom enter(Long userId, Long taskId) {

        this.members.put(userId, taskId);
        return this;
    }

    public TaskRoom exit(Long userId) {

        this.members.remove(userId);
        return this;
    }
}
