package com.modac.server.service;

import com.modac.server.domain.TaskRoom;
import com.modac.server.domain.TaskType;
import com.modac.server.domain.entity.Task;
import com.modac.server.domain.entity.User;
import com.modac.server.domain.message.Receive;
import com.modac.server.domain.message.Send;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.TaskRepository;
import com.modac.server.repository.UserRepository;
import com.modac.server.service.api.TaskApiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.util.HashMap;

@RequiredArgsConstructor
@Service
@Slf4j
public class StompService {

    private final TaskApiService taskApiService;
    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private final HashMap<TaskType, TaskRoom> taskRooms;

    @PostConstruct
    private void init() {
        for (TaskType taskType : TaskType.values()) {
            taskRooms.put(taskType, new TaskRoom(taskType));
            log.info("{} room created.", taskType);
        }
    }

    public Mono<Receive> join(Send send) {

        return userRepository.findById(send.getSenderId())
                .map(user -> taskApiService.activate(send.getTaskId(), send.getActionAt())
                        .map(task -> Receive.builder()
                                .senderId(user.getId())
                                .senderName(user.getName())
                                .senderProfile(user.getProfile())
                                .taskId(task.getId())
                                .taskStatus(task.getStatus())
                                .memberCount(taskRooms.get(task.getType()).enter(user.getId(), task.getId()).getMembers().size())
                                .payload(user.getName() + "이 입장했습니다.")
                                .build()))
                .orElseThrow(() -> new NotFoundException("no match user"));
    }

    public Mono<Receive> exit(Send send) {

        return userRepository.findById(send.getSenderId())
                .map(user -> taskApiService.inactivate(send.getTaskId(), send.getActionAt())
                        .map(task -> Receive.builder()
                                .senderId(user.getId())
                                .senderName(user.getName())
                                .senderProfile(user.getProfile())
                                .taskId(task.getId())
                                .taskStatus(task.getStatus())
                                .memberCount(taskRooms.get(task.getType()).exit(user.getId()).getMembers().size())
                                .payload(user.getName() + "이 퇴장했습니다.")
                                .build()))
                .orElseThrow(() -> new NotFoundException("no match user"));
    }

    public Mono<Receive> pause(Send send) {

        return userRepository.findById(send.getSenderId())
                .map(user -> taskApiService.pause(send.getTaskId(), send.getActionAt())
                        .map(task -> Receive.builder()
                                .senderId(user.getId())
                                .senderName(user.getName())
                                .senderProfile(user.getProfile())
                                .taskId(task.getId())
                                .taskStatus(task.getStatus())
                                .memberCount(taskRooms.get(task.getType()).getMembers().size())
                                .payload(send.getPayload())
                                .build()))
                .orElseThrow(() -> new NotFoundException("no match user"));
    }

    public Mono<Receive> resume(Send send) {

        return userRepository.findById(send.getSenderId())
                .map(user -> taskApiService.resume(send.getTaskId(), send.getActionAt())
                        .map(task -> Receive.builder()
                                .senderId(user.getId())
                                .senderName(user.getName())
                                .senderProfile(user.getProfile())
                                .taskId(task.getId())
                                .taskStatus(task.getStatus())
                                .memberCount(taskRooms.get(task.getType()).getMembers().size())
                                .payload(send.getPayload())
                                .build()))
                .orElseThrow(() -> new NotFoundException("no match user"));
    }

    public Mono<Receive> message(Send send) {

        return userRepository.findById(send.getSenderId())
                .map(user -> taskRepository.findById(send.getTaskId())
                        .map(task -> Receive.builder()
                                .senderId(user.getId())
                                .senderName(user.getName())
                                .senderProfile(user.getProfile())
                                .taskId(task.getId())
                                .taskStatus(task.getStatus())
                                .memberCount(taskRooms.get(task.getType()).getMembers().size())
                                .payload(send.getPayload())
                                .build())
                        .map(Mono::just)
                        .orElseThrow(() -> new NotFoundException("no match task")))
                .orElseThrow(() -> new NotFoundException("no match user"));
    }


}
