package com.modac.server.domain.entity;

import com.modac.server.converter.MultipartAttributeConverter;
import com.modac.server.domain.Multipart;
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
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String email;

    private String comment;

    @Convert(converter = MultipartAttributeConverter.class)
    private Multipart profile;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Task> tasks;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    private List<Record> records;
}
