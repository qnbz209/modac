package com.modac.server.repository;

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

    List<Task> findTasksByUserId(Pageable pageable, Long userId);

    @Query("SELECT SUM(totalDuration) FROM Task WHERE user=:user and type=:type")
    Long selectTotalDurationByType(User user, TaskType type);
}
