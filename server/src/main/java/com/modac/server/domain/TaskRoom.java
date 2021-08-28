package com.modac.server.domain;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;

@Data
@RequiredArgsConstructor
public class TaskRoom {

    /**
     * memberCount: 전체 member 수
     * members: 참여하고 있는 member들
     * taskType: Room의 unique key
     */
    private Integer memberCount;
    private final HashMap<Long, Long> members;
    private final TaskType type;

    public TaskRoom(TaskType type) {

        this.memberCount = 0;
        this.type = type;
        this.members = new HashMap<>();
    }

    public void enter(Long userId, Long taskId) {

        this.members.put(userId, taskId);
        this.memberCount += 1;
    }

    public void exit(Long userId) {

        this.members.remove(userId);
        this.memberCount -= 1;
    }
}
