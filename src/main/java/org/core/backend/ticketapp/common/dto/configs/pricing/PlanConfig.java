package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PlanConfig {
    private String plan;
    private List<PlatformFee> platformFee;
    private List<PaymentProcessingFee> paymentProcessingFee;
    private List<AdditionalFee> additionalFee;
    private List<Tax> tax;
    private List<CappedAmount> cappedAmount;
    private List<String> features;
    private List<String> support;
    private List<SubscriptionOption> subscriptionOption;
    //{
    //     "processor": "PayStack",
    //     "feeType": "PERCENTAGE",
    //     "amount": "3",
    //     "currency": "NGN"
    //}
    public double getTotalFees(final double amount , final String processor, final String currency){
//        final var platformFee = this.platformFee.stream()
//                .filter(platFee -> platFee.getProcessor().equalsIgnoreCase(processor)
//                        && platFee.getCurrency().equalsIgnoreCase(currency))
//                .map(platFee->{if (platFee.getFeeType().)});
        return 0;
    }
}
