package com.modac.server.controller;

import com.modac.server.domain.TaskType;
import com.modac.server.dto.TaskDetail;
import com.modac.server.dto.TaskSynthesis;
import com.modac.server.service.api.TaskApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/task")
@RequiredArgsConstructor
public class TaskApiController {

    private final TaskApiService taskApiService;

    @PostMapping("")
    public Mono<TaskDetail> create(@RequestParam String name, @RequestParam TaskType type, @RequestParam Long userId) {
        return taskApiService.create(name, type, userId);
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return taskApiService.delete(id);
    }

    @PutMapping("/{id}")
    public Mono<TaskDetail> update(@PathVariable Long id, @RequestParam String name, @RequestParam TaskType type) {
        return taskApiService.update(id, name, type);
    }

    @GetMapping("/{id}")
    public Mono<TaskDetail> getTaskDetailById(@PathVariable Long id) {
        return taskApiService.getTaskDetailById(id);
    }

    @GetMapping("/details/{id}")
    public Flux<TaskDetail> getTaskDetailsByUserId(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        return taskApiService.getTaskDetailsByUserId(page, size, id);
    }

    @GetMapping("/synthesis/{id}")
    public Mono<TaskSynthesis> getTaskSynthesisByUserId(@PathVariable Long id) {
        return taskApiService.getTaskSynthesisByUserId(id);
    }

}
