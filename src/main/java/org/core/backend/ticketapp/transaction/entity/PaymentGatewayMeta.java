package org.core.backend.ticketapp.transaction.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PaymentGatewayMeta {
    @JsonIgnore
    @Column(name = "gateway_response")
    private String gatewayResponse;
    @Column(name = "gateway")
    private String gateway;
    @Column(name = "channel")
    private String channel;
    @Column(name = "currency")
    private String currency;
    @Column(name = "ip_address")
    private String ipAddress;
    @Column(name = "created_at")
    private String createdAt;
    @Column(name = "paid_at")
    private String paidAt;
}