package com.modac.server.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;

@Accessors(chain = true)
@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class Reaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String emoji;

    @CreatedDate
    private LocalDateTime createdAt;

    @ManyToOne
    private User user;

    @ManyToOne
    private Record record;
}
