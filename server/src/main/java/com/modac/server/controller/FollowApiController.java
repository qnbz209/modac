package com.modac.server.controller;

import com.modac.server.domain.entity.Follow;
import com.modac.server.dto.UserThumbnail;
import com.modac.server.service.api.FollowApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/follow")
@RequiredArgsConstructor
public class FollowApiController {

    private final FollowApiService followApiService;

    @PostMapping("")
    public Mono<Follow> follow(@RequestParam Long follower, @RequestParam Long following) {
        return followApiService.follow(follower, following);
    }

    @DeleteMapping("")
    public Mono<String> unfollow(@RequestParam Long follower, @RequestParam Long following) {
        return followApiService.unfollow(follower, following);
    }

    @GetMapping("/er/{id}")
    public Flux<UserThumbnail> getFollowers(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        return followApiService.getFollowers(page, size, id);
    }

    @GetMapping("/ing/{id}")
    public Flux<UserThumbnail> getFollowings(@RequestParam int page, @RequestParam int size, @PathVariable Long id) {
        return followApiService.getFollowings(page, size, id);
    }
}
