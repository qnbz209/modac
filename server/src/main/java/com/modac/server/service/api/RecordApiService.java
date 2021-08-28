package com.modac.server.service.api;

import com.modac.server.domain.Multipart;
import com.modac.server.domain.TaskType;
import com.modac.server.domain.entity.Record;
import com.modac.server.dto.RecordDetail;
import com.modac.server.dto.RecordThumbnail;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.RecordRepository;
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
public class RecordApiService {

    private final RecordRepository recordRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Mono<RecordDetail> create(String name, String comment, TaskType type, Integer pausedCount, Long pausedTime,
                                     LocalDateTime startedAt, LocalDateTime finishedAt, Long userId, Long taskId) {

        return Mono.just(recordRepository.save(Record.builder()
                        .name(name)
                        .comment(comment)
                        .type(type)
                        .startedAt(startedAt)
                        .finishedAt(finishedAt)
                        .pausedCount(pausedCount)
                        .pausedTime(pausedTime)
                        .duration(Duration.between(startedAt, finishedAt).minus(Duration.of(pausedTime, ChronoUnit.SECONDS)).toSeconds())
                        .content(new Multipart())
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .user(userRepository.getById(userId))
                        .task(taskRepository.getById(taskId))
                        .build()))
                .map(this::builder);
    }

    public Mono<String> delete(Long id) {

        return recordRepository.findById(id)
                .map(record -> {
                    recordRepository.delete(record);
                    return Mono.just("Record deletion successes");
                })
            .orElseGet(() -> Mono.error(new NotFoundException("No match record")));
    }

    public Mono<RecordDetail> update(Long id, String name, String comment) {

        return recordRepository.findById(id)
                .map(record -> {
                    record.setName(name)
                          .setComment(comment)
                          .setUpdatedAt(LocalDateTime.now());
                    return record;
                })
                .map(recordRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("No match record")));
    }

    public Mono<RecordDetail> getRecordDetailById(Long id) {

        return recordRepository.findById(id)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("No match record")));
    }

    public Flux<RecordDetail> getRecordDetailsByUserId(int page, int size, Long userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<RecordDetail> recordDetails = new ArrayList<>();

        for (Record record : recordRepository.findRecordsByUserId(pageable, userId)) {
            recordDetails.add(RecordDetail.builder()
                    .id(record.getId())
                    .name(record.getName())
                    .comment(record.getComment())
                    .type(record.getType())
                    .startedAt(record.getStartedAt())
                    .finishedAt(record.getFinishedAt())
                    .pausedCount(record.getPausedCount())
                    .pausedTime(record.getPausedTime())
                    .duration(record.getDuration())
                    .content(record.getContent())
                    .build());
        }

        return Flux.fromIterable(recordDetails);
    }

    public Flux<RecordThumbnail> getRecordThumbnailsByUserId(int page, int size, Long userId) {

        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<RecordThumbnail> recordThumbnails = new ArrayList<>();

        for (Record record : recordRepository.findRecordsByUserId(pageable, userId)) {
            recordThumbnails.add(RecordThumbnail.builder()
                    .id(record.getId())
                    .content(record.getContent())
                    .build());
        }

        return Flux.fromIterable(recordThumbnails);
    }

    private RecordDetail builder(Record record) {
        return RecordDetail.builder()
                .id(record.getId())
                .name(record.getName())
                .comment(record.getComment())
                .type(record.getType())
                .startedAt(record.getStartedAt())
                .finishedAt(record.getFinishedAt())
                .pausedCount(record.getPausedCount())
                .pausedTime(record.getPausedTime())
                .duration(record.getDuration())
                .content(record.getContent())
                .build();
    }
}
