package com.modac.server.domain.entity;

import lombok.*;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Accessors(chain = true)
@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class Follow {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long follower;

    private Long following;
}
