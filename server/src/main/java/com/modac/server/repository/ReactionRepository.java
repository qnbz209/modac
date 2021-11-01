package com.modac.server.repository;

import com.modac.server.domain.entity.Reaction;
import com.modac.server.domain.entity.Record;
import com.modac.server.domain.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReactionRepository extends JpaRepository<Reaction, Long> {

    List<Reaction> findReactionsByUser(Pageable pageable, User user);

    List<Reaction> findReactionsByRecordAndEmoji(Pageable pageable, Record record, String emoji);

    List<Reaction> findAllByRecord(Record record);
}
