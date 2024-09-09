package org.core.backend.ticketapp.transaction.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class BankResponse {
    private boolean status;
    private String message;
    private List<Bank> data;

    @Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Bank {
        private int id;
        private String name;
        private String slug;
        private String code;
        private String longcode;
        private String gateway;
        private boolean pay_with_bank;
        private boolean supports_transfer;
        private boolean active;
        private String country;
        private String currency;
        private String type;
        private boolean is_deleted;
        private String createdAt;
        private String updatedAt;
    }
}
