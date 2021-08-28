package com.modac.server.domain.message;

import com.modac.server.domain.Multipart;
import com.modac.server.domain.TaskStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Builder
@Data
@NoArgsConstructor
public class Receive {

    private Long senderId;

    private String senderName;

    private Multipart senderProfile;

    private Long taskId;

    private TaskStatus taskStatus;

    private String payload;

    private Integer memberCount;
}
