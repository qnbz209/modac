package com.modac.server.service;

import com.modac.server.domain.Multipart;
import com.modac.server.dto.RecordDetail;
import com.modac.server.dto.UserProfile;
import com.modac.server.exception.BadRequestException;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.FollowRepository;
import com.modac.server.repository.RecordRepository;
import com.modac.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;

@RequiredArgsConstructor
@Service
@Slf4j
public class MultipartService {

    @Value("${spring.servlet.multipart.location}")
    private String fileLocation;

    private final FollowRepository followRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public Mono<UserProfile> uploadProfile(Long id, MultipartFile originalFile) {

        if (originalFile.isEmpty()) {
            return Mono.error(new BadRequestException("File is not selected!"));
        }

        try {
            return userRepository.findById(id)
                    .map(user -> {
                        String newFileName = fileLocation
                                + "/user_" + id + "_" + System.nanoTime()
                                + "_origin_" + originalFile.getOriginalFilename();

                        try {
                            File newFile = new File(newFileName);
                            originalFile.transferTo(newFile);

                            if (removeResource(user.getProfile())) {
                                log.info("Exist file deleted.");
                            }

                            user.setProfile(Multipart.builder()
                                            .contentType(originalFile.getContentType())
                                            .storedFileName(newFileName)
                                            .fileSize(originalFile.getSize())
                                            .build())
                                    .setUpdatedAt(LocalDateTime.now());

                        } catch (IOException e) {
                            e.printStackTrace();
                            throw new RuntimeException("File write failed!");
                        }

                        return user;
                    })
                    .map(userRepository::save)
                    .map(Mono::just)
                    .orElseGet(Mono::empty)
                    .map(user -> UserProfile.builder()
                            .id(user.getId())
                            .name(user.getName())
                            .email(user.getEmail())
                            .comment(user.getComment())
                            .profile(user.getProfile())
                            .follower(followRepository.countAllByFollowing(user.getId()))
                            .follower(followRepository.countAllByFollower(user.getId()))
                            .build())
                    .switchIfEmpty(Mono.error(new NotFoundException("no match user")));
        } catch (RuntimeException e) {
           return Mono.error(e);
        }
    }

    public Mono<RecordDetail> uploadRecordContent(Long id, MultipartFile originalFile) {

        if (originalFile.isEmpty()) {
            return Mono.error(new BadRequestException("File is not selected!"));
        }

        try {
        return recordRepository.findById(id)
                .map(record -> {
                    String newFileName = fileLocation
                            + "/record_" + id + "_" + System.nanoTime()
                            + "_origin_" + originalFile.getOriginalFilename();

                    try {
                        File newFile = new File(newFileName);
                        originalFile.transferTo(newFile);

                        if (removeResource(record.getContent())) {
                            log.info("Exist file deleted.");
                        }

                        record.setContent(Multipart.builder()
                                        .contentType(originalFile.getContentType())
                                        .storedFileName(newFileName)
                                        .fileSize(originalFile.getSize())
                                        .build())
                                .setUpdatedAt(LocalDateTime.now());
                        return record;
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return record;
                })
                .map(recordRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(record -> RecordDetail.builder()
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
                        .build())
                .switchIfEmpty(Mono.error(new NotFoundException("no match record")));
        } catch (RuntimeException e) {
            return Mono.error(e);
        }
    }

    public Mono<UserProfile> deleteProfile(Long id) {

        return userRepository.findById(id)
                .map(user -> {
                    Multipart profile = user.getProfile();
                    user.setProfile(new Multipart())
                            .setUpdatedAt(LocalDateTime.now());
                    removeResource(profile);
                    return user;
                })
                .map(userRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(user -> UserProfile.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .comment(user.getComment())
                        .follower(followRepository.countAllByFollowing(user.getId()))
                        .follower(followRepository.countAllByFollower(user.getId()))
                        .build())
                .switchIfEmpty(Mono.error(new NotFoundException("no match user")));
    }

    public Mono<RecordDetail> deleteRecordContent(Long id) {

        return recordRepository.findById(id)
                .map(record-> {
                    Multipart content = record.getContent();
                    record.setContent(new Multipart())
                            .setUpdatedAt(LocalDateTime.now());
                    removeResource(content);
                    return record;
                })
                .map(recordRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(record -> RecordDetail.builder()
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
                        .build())
                .switchIfEmpty(Mono.error(new NotFoundException("no match record")));
    }

    private Boolean removeResource(Multipart multipart) {

        if (multipart == null)
            return false;

        File file = new File(multipart.getStoredFileName());

        return file.exists() && file.delete();
    }
}
