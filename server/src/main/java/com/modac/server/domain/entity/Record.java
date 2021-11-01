package com.modac.server.domain.entity;

import com.modac.server.converter.MultipartAttributeConverter;
import com.modac.server.domain.Multipart;
import com.modac.server.domain.TaskType;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Accessors(chain = true)
@AllArgsConstructor
@Builder
@Entity
@Getter
@NoArgsConstructor
@Setter
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String comment;

    private LocalDateTime startedAt;

    private LocalDateTime finishedAt;

    private Integer pausedCount;

    private Long pausedTime;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart content;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "record")
    private List<Reaction> reactions;

    @ManyToOne
    private User user;

    @ManyToOne
    private Task task;
}
