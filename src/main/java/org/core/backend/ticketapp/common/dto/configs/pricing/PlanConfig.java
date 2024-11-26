package org.core.backend.ticketapp.common.dto.configs.pricing;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.core.backend.ticketapp.passport.service.core.PaymentProcessorType;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

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

    public TransactionFeesDTO getTransactionFeesDto(final double amount, final PaymentProcessorType processor, final String currency) {
        final var transactionProcessingFeesDTO = new TransactionFeesDTO();
        final var platformFee = this.platformFee.stream()
                .filter(fee -> fee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && fee.getCurrency().equalsIgnoreCase(currency))
                .map(platFee -> getAmountToCharge(platFee.getType(), platFee.getAmount(), amount))
                .mapToDouble(value -> value).sum();
        transactionProcessingFeesDTO.setPlatformFee(platformFee);
        final var processingFee = this.paymentProcessingFee.stream()
                .filter(fee -> fee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && fee.getCurrency().equalsIgnoreCase(currency))
                .map(proFee -> getAmountToCharge(proFee.getType(), proFee.getAmount(), amount))
                .mapToDouble(value -> value).sum();
        transactionProcessingFeesDTO.setProcessingFee(processingFee);
        final var tax = this.tax.stream()
                .filter(taxFee -> taxFee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && taxFee.getCurrency().equalsIgnoreCase(currency))
                .map(texFee1 -> getAmountToCharge(texFee1.getType(), texFee1.getAmount(), amount)).mapToDouble(value -> value).sum();
        transactionProcessingFeesDTO.setTax(tax);
        final var additionalFee = this.additionalFee.stream()
                .filter(taxFee -> taxFee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && taxFee.getCurrency().equalsIgnoreCase(currency))
                .map(texFee1 -> getAmountToCharge(texFee1.getType(), texFee1.getAmount(), amount)).mapToDouble(value -> value).sum();
        transactionProcessingFeesDTO.setAdditionalFee(additionalFee);
        final var totalFees = new BigDecimal(String.valueOf(platformFee))
                .add(new BigDecimal(String.valueOf(processingFee)))
                .add(new BigDecimal(String.valueOf(tax)))
                .add(new BigDecimal(String.valueOf(additionalFee)))
                .add(new BigDecimal(String.valueOf(amount)))
                .doubleValue();
        transactionProcessingFeesDTO.setTotalFes(totalFees);
        transactionProcessingFeesDTO.setPrice(amount);
        return transactionProcessingFeesDTO;
    }

    private double getAmountToCharge(final FeeType feeType, final String value, final double amount) {
        if (Objects.requireNonNull(feeType) == FeeType.PERCENTAGE) {
            final var percentageVal = new BigDecimal(String.valueOf(value)).divide(new BigDecimal(String.valueOf(100)), RoundingMode.UNNECESSARY);
            return percentageVal.multiply(new BigDecimal(String.valueOf(amount))).doubleValue();
        } else if (Objects.requireNonNull(feeType) == FeeType.FIXED) {
            final var percentageVal = new BigDecimal(String.valueOf(value));
            return percentageVal.doubleValue();
        } else if (Objects.requireNonNull(feeType) == FeeType.RANGE) {
            return 0; //not implemented at the moment
        }
        return 0;
    }

}
