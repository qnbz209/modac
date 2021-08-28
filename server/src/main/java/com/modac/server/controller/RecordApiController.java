package com.modac.server.controller;

import com.modac.server.domain.TaskType;
import com.modac.server.dto.RecordDetail;
import com.modac.server.dto.RecordThumbnail;
import com.modac.server.service.api.RecordApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/record")
@RequiredArgsConstructor
public class RecordApiController {

    private final RecordApiService recordApiService;

    @PostMapping("")
    public Mono<RecordDetail> create(@RequestParam String name, @RequestParam String comment,
                                     @RequestParam TaskType type, @RequestParam Integer pausedCount, @RequestParam Long pausedTime,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startedAt,
                                     @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime finishedAt,
                                     @RequestParam Long userId, @RequestParam Long taskId) {
        return recordApiService.create(name, comment, type, pausedCount, pausedTime, startedAt, finishedAt, userId, taskId);
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return recordApiService.delete(id);
    }

    @PutMapping("/{id}")
    public Mono<RecordDetail> update(@PathVariable Long id, @RequestParam String name, @RequestParam String comment) {
        return recordApiService.update(id, name, comment);
    }

    @GetMapping("/{id}")
    public Mono<RecordDetail> getRecordDetailById(@PathVariable Long id) {
        return recordApiService.getRecordDetailById(id);
    }

    @GetMapping("/details/{id}")
    public Flux<RecordDetail> getRecordDetailsByUserId(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        return recordApiService.getRecordDetailsByUserId(page, size, id);
    }

    @GetMapping("/thumbnail/{id}")
    public Flux<RecordThumbnail> getRecordThumbnailsByUserId(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        return recordApiService.getRecordThumbnailsByUserId(page, size, id);
    }
}
