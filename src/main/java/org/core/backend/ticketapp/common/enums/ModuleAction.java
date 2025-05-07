package org.core.backend.ticketapp.common.enums;

public enum ModuleAction {
    CREATE_EVENT("create_event", "event"),
    UPDATE_EVENT("update_event", "event"),
    DELETE_EVENT("delete_event", "event"),
    CREATE_PAYMENT("create_payment", "transaction"),
    INIT_TRANSACTION("init_transaction", "transaction");
    final String action;
    final String module;

    ModuleAction(String action, String module) {
        this.action = action;
        this.module = module;
    }

    public String getAction() {
        return action;
    }

    public String getModule() {
        return module;
    }
}
