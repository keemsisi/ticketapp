package org.core.backend.ticketapp.passport.dtos.notification;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sun.istack.NotNull;
import lombok.*;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
@Getter
@Setter
public class DividendRequestDto {
    @NotNull
    private UUID clientCompanyId;

    @NotNull
    private UUID dividendTypeId;

    @NotNull
    private LocalDateTime qualifyDate;

    @NotNull
    private LocalDateTime settlementDate;

    @NotNull
    private Double tax;

    @NotBlank(message = "Year ending is required")
    private String yearEnding;

    @NotNull
    private Date payableDate;

    @NotNull
    private Double rate;

    private String bigHoldingValue;

    @NotBlank(message = "Dividend bank payment account number is required")
    private String dividendPaymentBankAccountNumber;

    @NotBlank(message = "Dividend bank payment account name is required")
    private String dividendPaymentBankName;

    @NotBlank(message = "Dividend narration is required")
    private String divNarration;

    @NotNull
    private Long invalidationDuration;

    private String warrantNumber;

    private String divIssueType;

}
