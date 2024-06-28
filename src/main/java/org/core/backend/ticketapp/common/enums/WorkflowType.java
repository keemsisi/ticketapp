package org.core.backend.ticketapp.common.enums;

public enum WorkflowType {
    DIVIDEND_APPROVAL("dividend approval"),
    LOCK_USER("Lock User"),
    DEFAULT_WORKFLOW("default workflow");
    String s ;
    WorkflowType(String s) {
        this.s = s ;
    }

    public String getS() {
        return s;
    }
}