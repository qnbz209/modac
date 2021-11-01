package com.modac.server.controller.api;

import com.modac.server.dto.ReactionUser;
import com.modac.server.service.api.ReactionApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@RequestMapping("/api/reaction")
@RestController
public class ReactionApiController {

    private final ReactionApiService reactionApiService;

    @PostMapping("")
    public Mono<Void> create(@RequestParam Long userId, @RequestParam Long recordId, @RequestParam String emoji) {
        return reactionApiService.create(userId, recordId, emoji);
    }

    @DeleteMapping("/{id}")
    public Mono<String> delete(@PathVariable Long id) {
        return reactionApiService.delete(id);
    }

    @GetMapping("/user/{id}")
    public Flux<ReactionUser> getReactionUser(@RequestParam int page, @RequestParam int size, @PathVariable Long id, @RequestParam String emoji) {
        return reactionApiService.getReactionUsers(page, size, id, emoji);
    }
}
