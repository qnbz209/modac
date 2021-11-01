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
import java.time.LocalDateTime;
import java.util.Map;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class RecordDetail {

    private Long id;

    private String name;

    private String comment;

    @Enumerated(value = EnumType.STRING)
    private TaskType type;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Integer pausedCount;

    private Long pausedTime;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart content;

    private Map<String, Integer> reactions;
}
