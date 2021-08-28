package com.modac.server.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Multipart {

    private String contentType;

    private String storedFileName;

    private Long fileSize;
}
