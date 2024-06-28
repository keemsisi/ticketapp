package org.core.backend.ticketapp.common;


import java.util.Date;

public class GenericResponse<T> {

    private String status, statusMessage;
    private T data;
    private Date date = new Date();

    public GenericResponse() {
    }

    public GenericResponse(String status, String statusMessage, T data) {
        this.status = status;
        this.statusMessage = statusMessage;
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public static final String SUCCESS_STATUS_FLAG = "00";
    public static final String ERROR_STATUS_FLAG = "01";
}
