package com.modac.server.dto;

import com.modac.server.converter.MultipartAttributeConverter;
import com.modac.server.domain.Multipart;
import com.modac.server.domain.entity.User;
import com.modac.server.repository.FollowRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserProfile {

    private Long id;

    private String name;

    private String email;

    private String comment;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart profile;

    private Integer follower;

    private Integer following;
}
