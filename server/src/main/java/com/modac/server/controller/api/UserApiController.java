package com.modac.server.controller.api;

import com.modac.server.dto.UserProfile;
import com.modac.server.service.api.UserApiService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserApiController {

    private final UserApiService userApiService;

    @PostMapping("")
    public Mono<UserProfile> signup(@RequestParam String name, @RequestParam String email, @RequestParam String comment) {
        return userApiService.signup(name, email, comment);
    }

    @GetMapping("/login")
    public Mono<UserProfile> login(@RequestParam String email) {
        return userApiService.login(email);
    }

    @GetMapping("/profile/{id}")
    public Mono<UserProfile> getProfile(@PathVariable Long id) {
        return userApiService.getProfile(id);
    }

    @PutMapping("/{id}")
    public Mono<UserProfile> updateProfile(@PathVariable Long id, @RequestParam String name, @RequestParam String comment) {
        return userApiService.updateProfile(id, name, comment);
    }
}
