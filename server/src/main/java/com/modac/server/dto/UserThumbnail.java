package com.modac.server.dto;

import com.modac.server.converter.MultipartAttributeConverter;
import com.modac.server.domain.Multipart;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class UserThumbnail {

    private Long id;

    private String name;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart profile;
}
