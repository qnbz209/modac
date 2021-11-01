package com.modac.server.dto;

import com.modac.server.converter.MultipartAttributeConverter;
import com.modac.server.domain.Multipart;
import com.modac.server.domain.TaskType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Convert;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RecordThumbnail {

    private Long id;

    private String name;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;

    private Long duration;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart content;
}
