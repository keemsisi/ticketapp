package org.core.backend.ticketapp.passport.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PublisherPropertyDTO {
    private String topicName;
    private String subscriptName;
    private Object message;
    private String messageId;
}