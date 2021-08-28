package com.modac.server.repository;

import com.modac.server.domain.entity.Follow;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<Follow, Long> {

    Integer countAllByFollower(Long id);

    Integer countAllByFollowing(Long id);

    List<Follow> findFollowsByFollower(PageRequest pageRequest, Long follower);

    List<Follow> findFollowsByFollowing(PageRequest pageRequest, Long followed);

    Optional<Follow> findByFollowerAndFollowing(Long follower, Long following);
}
