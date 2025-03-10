package org.core.backend.ticketapp.common.response;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import org.apache.commons.lang3.ObjectUtils;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class EventStatsDTO {
    private Long totalPending;
    private Long totalSuccessful;
    private Long totalCancelled;
    private Long totalFailed;

    @JsonGetter(value = "totalPending")
    public Long getTotalPending() {
        return ObjectUtils.defaultIfNull(totalPending, 0L);
    }

    @JsonGetter(value = "totalSuccessful")
    public Long getTotalSuccessful() {
        return ObjectUtils.defaultIfNull(totalSuccessful, 0L);
    }

    @JsonGetter(value = "totalCancelled")
    public Long getTotalCancelled() {
        return ObjectUtils.defaultIfNull(totalCancelled, 0L);
    }

    @JsonGetter(value = "totalFailed")
    public Long getTotalFailed() {
        return ObjectUtils.defaultIfNull(totalFailed, 0L);
    }
}