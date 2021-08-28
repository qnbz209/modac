package com.modac.server.repository;

import com.modac.server.domain.TaskRoom;
import com.modac.server.domain.TaskType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
@Slf4j
public class StompRepository {

    private final Map<TaskType, TaskRoom> taskRoomMap = new HashMap<>();

    public StompRepository() {

        for (TaskType taskType : TaskType.values()) {
            taskRoomMap.put(taskType, new TaskRoom(taskType));
            log.info("{} room created.", taskType);
        }
    }

    public TaskRoom getRoom(TaskType taskType) {

        return taskRoomMap.get(taskType);
    }

    public TaskRoom joinRoom(Long userId, Long taskId, TaskRoom taskRoom) {

        taskRoom.enter(userId, taskId);
        return taskRoom;
    }

    public TaskRoom exitRoom(Long userId, TaskRoom taskRoom) {

        taskRoom.exit(userId);
        return taskRoom;
    }
}
