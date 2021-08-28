package com.modac.server.service.api;

import com.modac.server.dto.UserProfile;
import com.modac.server.domain.entity.User;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.FollowRepository;
import com.modac.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserApiService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public Mono<UserProfile> signup(String name, String email, String comment) {
        return Mono.just(userRepository.save(User.builder()
                        .name(name)
                        .email(email)
                        .comment(comment)
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()))
                .map(this::builder);
    }

    public Mono<String> unregister(Long id) {
        return userRepository.findById(id)
                .map(user -> {
                    userRepository.delete(user);
                    return Mono.just("User deletion successes!");
                })
                .orElseGet(() -> Mono.error(new NotFoundException("no match user")));
    }

    public Mono<UserProfile> login(String email) {
        return userRepository.findByEmail(email)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("no match user!")));
    }

    public Mono<UserProfile> getProfile(Long id) {
        return userRepository.findById(id)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("no match user!")));
    }

    public Mono<UserProfile> updateProfile(Long id, String name, String comment) {
        return userRepository.findById(id)
                .map(user -> {
                    user.setName(name)
                        .setComment(comment)
                        .setUpdatedAt(LocalDateTime.now());
                    return user;
                })
                .map(userRepository::save)
                .map(Mono::just)
                .orElseGet(Mono::empty)
                .map(this::builder)
                .switchIfEmpty(Mono.error(new NotFoundException("no match user!")));
    }

    private UserProfile builder(User user) {
        return UserProfile.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .comment(user.getComment())
                .follower(followRepository.countAllByFollowing(user.getId()))
                .follower(followRepository.countAllByFollower(user.getId()))
                .build();
    }
}
