package com.hrms.dto;

import java.time.Instant;
import java.util.List;

public class ApiResponse<T> {
    private boolean success;
    private int status;
    private String message;
    private String clientMessageId;
    private long timestamp;
    private T data;
    public ApiResponse() { }

    public ApiResponse(boolean success, int status, String message, String clientMessageId, long timestamp, T data, List<String> errors) {
        this.success = success;
        this.status = status;
        this.message = message;
        this.clientMessageId = clientMessageId;
        this.timestamp = timestamp;
        this.data = data;
    }

    public static <T> ApiResponse<T> of(boolean success, int status, String message, String clientMessageId, T data, List<String> errors) {
        return new ApiResponse<>(success, status, message, clientMessageId, Instant.now().toEpochMilli(), data, errors);
    }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }

    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }

    public String getClientMessageId() { return clientMessageId; }
    public void setClientMessageId(String clientMessageId) { this.clientMessageId = clientMessageId; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public T getData() { return data; }
    public void setData(T data) { this.data = data; }
}
