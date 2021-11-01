package com.modac.server.service.api;

import com.modac.server.domain.entity.Reaction;
import com.modac.server.domain.entity.Record;
import com.modac.server.dto.ReactionUser;
import com.modac.server.exception.NotFoundException;
import com.modac.server.repository.ReactionRepository;
import com.modac.server.repository.RecordRepository;
import com.modac.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class ReactionApiService {

    private final ReactionRepository reactionRepository;
    private final RecordRepository recordRepository;
    private final UserRepository userRepository;

    public Mono<Void> create(Long userId, Long recordId, String emoji) {

        return Mono.just(reactionRepository.save(Reaction.builder()
                .emoji(emoji)
                .createdAt(LocalDateTime.now())
                .record(recordRepository.getById(recordId))
                .user(userRepository.getById(userId))
                .build()))
                .then();
    }

    public Mono<String> delete(Long id) {

        return reactionRepository.findById(id)
                .map(reaction -> {
                    reactionRepository.delete(reaction);
                    return Mono.just("Reaction deleted.");
                })
                .orElseGet(() -> Mono.error(new NotFoundException("no match data")));
    }

    public Flux<ReactionUser> getReactionUsers(int page, int size, Long recordId, String emoji) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by("createdAt").descending());
        List<ReactionUser> reactionUsers = new ArrayList<>();

        for (Reaction reaction : reactionRepository.findReactionsByRecordAndEmoji(pageRequest, recordRepository.getById(recordId), emoji)) {
            reactionUsers.add(ReactionUser.builder()
                    .id(reaction.getUser().getId())
                    .name(reaction.getUser().getName())
                    .profile(reaction.getUser().getProfile())
                    .createdAt(reaction.getCreatedAt())
                    .build());
        }

        return Flux.fromIterable(reactionUsers);
    }
}
