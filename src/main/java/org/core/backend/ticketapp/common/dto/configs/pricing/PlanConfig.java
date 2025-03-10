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
                .mapToDouble(BigDecimal::doubleValue).sum();
        transactionProcessingFeesDTO.setPlatformFee(platformFee);
        final var processingFee = this.paymentProcessingFee.stream()
                .filter(fee -> fee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && fee.getCurrency().equalsIgnoreCase(currency))
                .map(proFee -> getAmountToCharge(proFee.getType(), proFee.getAmount(), amount))
                .mapToDouble(BigDecimal::doubleValue).sum();
        transactionProcessingFeesDTO.setProcessingFee(processingFee);
        final var tax = this.tax.stream()
                .filter(taxFee -> taxFee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && taxFee.getCurrency().equalsIgnoreCase(currency))
                .map(texFee1 -> getAmountToCharge(texFee1.getType(), texFee1.getAmount(), amount))
                .mapToDouble(BigDecimal::doubleValue).sum();
        transactionProcessingFeesDTO.setTax(tax);
        final var additionalFee = this.additionalFee.stream()
                .filter(taxFee -> taxFee.getProcessor().equalsIgnoreCase(processor.name().toLowerCase()) && taxFee.getCurrency().equalsIgnoreCase(currency))
                .map(texFee1 -> getAmountToCharge(texFee1.getType(), texFee1.getAmount(), amount))
                .mapToDouble(BigDecimal::doubleValue).sum();
        transactionProcessingFeesDTO.setAdditionalFee(additionalFee);
        final var totalFees = new BigDecimal(String.valueOf(platformFee))
                .add(new BigDecimal(String.valueOf(processingFee)))
                .add(new BigDecimal(String.valueOf(tax)))
                .add(new BigDecimal(String.valueOf(additionalFee)));
        final var totalCost = totalFees
                .add(new BigDecimal(String.valueOf(amount)))
                .setScale(2, RoundingMode.CEILING)  // Always round up to 2 decimal places
                .doubleValue();
        transactionProcessingFeesDTO.setTotalCost(totalCost);
        transactionProcessingFeesDTO.setTotalFees(totalFees.doubleValue());
        transactionProcessingFeesDTO.setPrice(amount);
        return transactionProcessingFeesDTO;
    }

    private BigDecimal getAmountToCharge(final FeeType feeType, final String value, final double amount) {
        if (Objects.requireNonNull(feeType) == FeeType.PERCENTAGE) {
            final var percentageVal = new BigDecimal(value).divide(new BigDecimal("100"));
            return percentageVal.multiply(new BigDecimal(String.valueOf(amount)));
        } else if (Objects.requireNonNull(feeType) == FeeType.FIXED) {
            return new BigDecimal(String.valueOf(value));
        } else if (Objects.requireNonNull(feeType) == FeeType.RANGE) {
            return BigDecimal.ZERO; //not implemented at the moment
        }
        return BigDecimal.ZERO;
    }
}
