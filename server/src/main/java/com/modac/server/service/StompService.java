package com.modac.server.service;

import com.modac.server.domain.entity.User;
import com.modac.server.domain.message.Receive;
import com.modac.server.domain.message.Send;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.StompRepository;
import com.modac.server.repository.TaskRepository;
import com.modac.server.repository.UserRepository;
import com.modac.server.service.api.TaskApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Service
public class StompService {

    private final TaskApiService taskApiService;
    private final StompRepository stompRepository;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    public Mono<Receive> join(Send send) {

        try {
            User user = userRepository.findById(send.getSenderId())
                    .orElseThrow(() -> new NotFoundException("no match user"));

            return taskApiService.activate(send.getSenderId(), send.getActionAt())
                    .map(task -> Receive.builder()
                            .senderId(user.getId())
                            .senderName(user.getName())
                            .senderProfile(user.getProfile())
                            .taskId(task.getId())
                            .taskStatus(task.getStatus())
                            .memberCount(stompRepository.joinRoom(user.getId(), task.getId(),
                                    stompRepository.getRoom(task.getType())).getMemberCount())

                            .payload(send.getPayload())
                            .build());
        } catch (NotFoundException e) {
            return Mono.error(e);
        }
    }

    public Mono<Receive> exit(Send send) {

        try {
            User user = userRepository.findById(send.getSenderId())
                    .orElseThrow(() -> new NotFoundException("no match user"));

            return taskApiService.inactivate(send.getSenderId(), send.getActionAt())
                    .map(task -> Receive.builder()
                            .senderId(user.getId())
                            .senderName(user.getName())
                            .senderProfile(user.getProfile())
                            .taskId(task.getId())
                            .taskStatus(task.getStatus())
                            .memberCount(stompRepository.exitRoom(user.getId(),
                                    stompRepository.getRoom(task.getType())).getMemberCount())
                            .payload(send.getPayload())
                            .build());
        } catch (NotFoundException e) {
            return Mono.error(e);
        }
    }

    public Mono<Receive> pause(Send send) {

        try {
            User user = userRepository.findById(send.getSenderId())
                    .orElseThrow(() -> new NotFoundException("no match user"));

            return taskApiService.pause(send.getSenderId(), send.getActionAt())
                    .map(task -> Receive.builder()
                            .senderId(user.getId())
                            .senderName(user.getName())
                            .senderProfile(user.getProfile())
                            .taskId(task.getId())
                            .taskStatus(task.getStatus())
                            .memberCount(stompRepository.getRoom(task.getType()).getMemberCount())
                            .payload(send.getPayload())
                            .build());
        } catch (NotFoundException e) {
            return Mono.error(e);
        }
    }

    public Mono<Receive> resume(Send send) {

        try {
            User user = userRepository.findById(send.getSenderId())
                    .orElseThrow(() -> new NotFoundException("no match user"));

            return taskApiService.resume(send.getSenderId(), send.getActionAt())
                    .map(task -> Receive.builder()
                            .senderId(user.getId())
                            .senderName(user.getName())
                            .senderProfile(user.getProfile())
                            .taskId(task.getId())
                            .taskStatus(task.getStatus())
                            .memberCount(stompRepository.getRoom(task.getType()).getMemberCount())
                            .payload(send.getPayload())
                            .build());
        } catch (NotFoundException e) {
            return Mono.error(e);
        }
    }

    public Mono<Receive> message(Send send) {

        try {
            User user = userRepository.findById(send.getSenderId())
                    .orElseThrow(() -> new NotFoundException("no match user"));

            return taskRepository.findById(send.getTaskId())
                    .map(task -> Receive.builder()
                            .senderId(user.getId())
                            .senderName(user.getName())
                            .senderProfile(user.getProfile())
                            .taskId(task.getId())
                            .taskStatus(task.getStatus())
                            .memberCount(stompRepository.getRoom(task.getType()).getMemberCount())
                            .payload(send.getPayload())
                            .build())
                    .map(Mono::just)
                    .orElseThrow(() -> new NotFoundException(("no match task")));
        } catch (NotFoundException e) {
            return Mono.error(e);
        }
    }
}
