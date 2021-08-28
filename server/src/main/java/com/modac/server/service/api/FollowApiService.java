package com.modac.server.service.api;

import com.modac.server.domain.entity.Follow;
import com.modac.server.domain.entity.User;
import com.modac.server.dto.UserThumbnail;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.FollowRepository;
import com.modac.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class FollowApiService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;

    public Mono<Follow> follow(Long follower, Long following) {

        return Mono.just(followRepository.save(Follow.builder()
                .follower(follower)
                .following(following)
                .build()));
    }

    public Mono<String> unfollow(Long follower, Long following) {

        return followRepository.findByFollowerAndFollowing(follower, following)
                .map(follow -> {
                    followRepository.delete(follow);
                    return Mono.just("Unfollow successes");
                })
                .orElseGet(() -> Mono.error(new NotFoundException("no match data")));
    }


    public Flux<UserThumbnail> getFollowers(int page, int size, Long id) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        List<UserThumbnail> followers = new ArrayList<>();

        for (Follow follow : followRepository.findFollowsByFollowing(pageRequest, id)) {
            User user = userRepository.getById(follow.getFollower());

            followers.add(UserThumbnail.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .profile(user.getProfile())
                    .build());
        }

        return Flux.fromIterable(followers);
    }

    public Flux<UserThumbnail> getFollowings(int page, int size, Long id) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("updatedAt").descending());
        List<UserThumbnail> followings = new ArrayList<>();

        for (Follow follow : followRepository.findFollowsByFollower(pageRequest, id)) {
            User user = userRepository.getById(follow.getFollower());

            followings.add(UserThumbnail.builder()
                    .id(user.getId())
                    .name(user.getName())
                    .profile(user.getProfile())
                    .build());
        }

        return Flux.fromIterable(followings);
    }
}
