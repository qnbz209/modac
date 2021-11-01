package com.modac.server.repository;

import com.modac.server.domain.TaskState;
import com.modac.server.domain.TaskType;
import com.modac.server.domain.entity.Task;
import com.modac.server.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findTasksByUserIdAndState(Pageable pageable, Long userId, TaskState state);

    List<Task> findTasksByUserId(Pageable pageable, Long userId);

    @Query("SELECT SUM(task.totalDuration) FROM Task task WHERE task.type=:type AND task.user=:user")
    Long getTotalDurationByUserAndType(User user, TaskType type);

    @Query("SELECT SUM(task.totalDuration) FROM Task task WHERE task.type=:type AND task.user=:user AND task.state<>:HIDDEN")
    Long getNotHiddenTotalDurationByUserAndType(User user, TaskType type);
}
