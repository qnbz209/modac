package com.modac.server.controller;

import com.modac.server.domain.message.Send;
import com.modac.server.service.StompService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
@RequiredArgsConstructor
public class SocketController {

    private final SimpMessagingTemplate template;
    private final StompService stompService;
    private final String DESTINATION = "/subscribe/task/";

    @MessageMapping("/task/join/{taskType}")
    public void join(@RequestParam Send send, @PathVariable String taskType) {
        template.convertAndSend(DESTINATION + taskType, stompService.join(send));
    }

    @MessageMapping("/task/exit/{taskType}")
    public void exit(@RequestParam Send send, @PathVariable String taskType) {
        template.convertAndSend(DESTINATION + taskType, stompService.exit(send));
    }

    @MessageMapping("/task/pause/{taskType}")
    public void pause(@RequestParam Send send, @PathVariable String taskType) {
        template.convertAndSend(DESTINATION + taskType, stompService.pause(send));
    }

    @MessageMapping("/task/resume/{taskType}")
    public void resume(@RequestParam Send send, @PathVariable String taskType) {
        template.convertAndSend(DESTINATION + taskType, stompService.resume(send));
    }

    @MessageMapping("/task/message/{taskType}")
    public void message(@RequestParam Send send, @PathVariable String taskType) {
        template.convertAndSend(DESTINATION + taskType, stompService.message(send));
    }
}
