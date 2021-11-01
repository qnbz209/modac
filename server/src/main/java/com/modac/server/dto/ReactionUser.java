package com.modac.server.dto;

import com.modac.server.converter.MultipartAttributeConverter;
import com.modac.server.domain.Multipart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import java.time.LocalDateTime;
@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class ReactionUser {

    private Long id;

    private String name;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart profile;

    private LocalDateTime createdAt;
}
