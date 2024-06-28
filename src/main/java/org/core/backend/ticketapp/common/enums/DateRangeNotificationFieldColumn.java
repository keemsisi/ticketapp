package org.core.backend.ticketapp.common.enums;

public enum DateRangeNotificationFieldColumn {
    DATE_CREATED("date_created"),
    DATE_APPROVED("date_approved"),
    DATE_REQUESTED("date_requested");
    String columnName;
    DateRangeNotificationFieldColumn(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnName() {
        return columnName;
    }
}
