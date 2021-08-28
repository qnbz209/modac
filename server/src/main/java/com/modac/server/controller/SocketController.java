package com.modac.server.controller;

import com.modac.server.domain.message.Send;
import com.modac.server.service.StompService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate template;
    private final StompService stompService;

    @MessageMapping("/task/join/{taskType}")
    public void join(Send send, @PathVariable String taskType) {
        template.convertAndSend("/subscribe/task/" + taskType, stompService.join(send));
    }

    @MessageMapping("/task/exit/{taskType}")
    public void exit(Send send, @PathVariable String taskType) {
        template.convertAndSend("/subscribe/task/" + taskType, stompService.exit(send));
    }

    @MessageMapping("/task/pause/{taskType}")
    public void pause(Send send, @PathVariable String taskType) {
        template.convertAndSend("/subscribe/task/" + taskType, stompService.pause(send));
    }

    @MessageMapping("/task/resume/{taskType}")
    public void resume(Send send, @PathVariable String taskType) {
        template.convertAndSend("/subscribe/task/" + taskType, stompService.resume(send));
    }

    @MessageMapping("/task/message/{taskType}")
    public void message(Send send, @PathVariable String taskType) {
        template.convertAndSend("/subscribe/task/" + taskType, stompService.message(send));
    }
}
