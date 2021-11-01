package com.modac.server.service.api;

import com.modac.server.domain.TaskState;
import com.modac.server.domain.TaskStatus;
import com.modac.server.domain.TaskType;
import com.modac.server.domain.entity.Task;
import com.modac.server.dto.TaskDetail;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.TaskRepository;
import com.modac.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class TaskApiService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Mono<TaskDetail> create(String name, TaskType type, Long userId) {

        return Mono.just(taskRepository.save(Task.builder()
                        .name(name)
                        .type(type)
                        .status(TaskStatus.INACTIVE)
                        .state(TaskState.PROGRESS)
                        .continuous(0)
                        .pauseCount(0)
                        .pausedTime(0L)
                        .startedAt(null)
                        .pausedAt(null)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .user(userRepository.getById(userId))
                        .build()))
                .map(this::builder);
    }

    public Mono<String> delete(Long id) {

        return taskRepository.findById(id)
                .map(task -> {
                    taskRepository.delete(task);
                    return Mono.just("Task deletion successes");
                })
                .orElseGet(() -> Mono.error(new NotFoundException("No match task")));
    }

    public Mono<TaskDetail> update(Long id, String name, TaskType type, TaskState state) {

        return taskRepository.findById(id)
                .map(task -> {
                    task.setName(name)
                            .setType(type)
                            .setState(state)
                            .setUpdatedAt(LocalDateTime.now());
                    return task;
                })
                .map(taskRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("No match task")));
    }

    public Mono<Task> activate(Long id, LocalDateTime startedAt) {
        return taskRepository.findById(id)
                .map(task -> {
                    boolean isContinuous = (task.getLastDoneAt() != null) && (Duration.between(task.getLastDoneAt(), startedAt).getSeconds() <= 86400L);
                    task.setStatus(TaskStatus.ACTIVE)
                            .setContinuous(isContinuous ? 0 : (task.getContinuous() + 1))
                            .setStartedAt(startedAt)
                            .setUpdatedAt(LocalDateTime.now());
                    return task;
                })
                .map(taskRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .switchIfEmpty(Mono.error(new NotFoundException("No match task")));
    }

    public Mono<Task> inactivate(Long id, LocalDateTime inactiveAt) {

        return taskRepository.findById(id)
                .map(task -> {
                    Long totalDuration = task.getTotalDuration();
                    Long pausedTime = task.getPausedTime();
                    LocalDateTime startedAt = task.getStartedAt();

                    task.setStatus(TaskStatus.INACTIVE)
                            .setPauseCount(0)
                            .setPausedTime(0L)
                            .setStartedAt(null)
                            .setPausedAt(null)
                            .setLastDoneAt(inactiveAt)
                            .setUpdatedAt(LocalDateTime.now())
                            .setTotalDuration(totalDuration + Duration.between(startedAt, inactiveAt).minus(Duration.of(pausedTime, ChronoUnit.SECONDS)).toSeconds());
                    return task;
                })
                .map(taskRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .switchIfEmpty(Mono.error(new NotFoundException("No match task")));
    }

    public Mono<Task> pause(Long id, LocalDateTime pausedAt) {

        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(TaskStatus.PAUSE)
                            .setPauseCount(task.getPauseCount() + 1)
                            .setPausedAt(pausedAt)
                            .setUpdatedAt(LocalDateTime.now());
                    return task;
                })
                .map(taskRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .switchIfEmpty(Mono.error(new NotFoundException("No match task")));
    }

    public Mono<Task> resume(Long id, LocalDateTime resumedAt) {

        return taskRepository.findById(id)
                .map(task -> {
                    task.setStatus(TaskStatus.ACTIVE)
                            .setPausedTime(task.getPausedTime() + (Duration.between(task.getPausedAt(), resumedAt)).toSeconds())
                            .setUpdatedAt(LocalDateTime.now());
                    return task;
                })
                .map(taskRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .switchIfEmpty(Mono.error(new NotFoundException("No match task")));
    }

    public Mono<TaskDetail> getTaskDetailById(Long id) {
        return taskRepository.findById(id)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("No match task")));
    }

    public Flux<TaskDetail> getTaskDetailsByUserId(int page, int size, Long userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<TaskDetail> taskDetails = new ArrayList<>();

        for (Task task : taskRepository.findTasksByUserId(pageable, userId)) {
            taskDetails.add(builder(task));
        }

        return Flux.fromIterable(taskDetails);
    }

    public Flux<TaskDetail> getTaskDetailsByUserIdAndState(int page, int size, Long userId, TaskState state) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<TaskDetail> taskDetails = new ArrayList<>();

        for (Task task : taskRepository.findTasksByUserIdAndState(pageable, userId, state)) {
            taskDetails.add(builder(task));
        }

        return Flux.fromIterable(taskDetails);
    }

    private TaskDetail builder(Task task) {
        return TaskDetail.builder()
                .id(task.getId())
                .name(task.getName())
                .type(task.getType())
                .status(task.getStatus())
                .state(task.getState())
                .continuous(task.getContinuous())
                .pauseCount(task.getPauseCount())
                .pausedTime(task.getPausedTime())
                .startedAt(task.getStartedAt())
                .pausedAt(task.getPausedAt())
                .lastDoneAt(task.getLastDoneAt())
                .totalDuration(task.getTotalDuration())
                .build();
    }
}

