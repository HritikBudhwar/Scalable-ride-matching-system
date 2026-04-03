package com.platform.dto.response;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for admin responses
 */
public class AdminResponseDTO {
    private String action;
    private Long targetId;
    private String status; // SUCCESS, FAILED, PENDING
    private String message;
    private LocalDateTime timestamp;
    private String performedBy;
    private Object data;
    private String errorCode;
    private Boolean requiresFollowUp;
    private String followUpAction;
    
    public AdminResponseDTO() {
        // Default constructor
    }
    
    public AdminResponseDTO(String action, Long targetId, String status, String message, String performedBy) {
        this.action = action;
        this.targetId = targetId;
        this.status = status;
        this.message = message;
        this.performedBy = performedBy;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Long getTargetId() {
        return targetId;
    }
    
    public void setTargetId(Long targetId) {
        this.targetId = targetId;
    }
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }
    
    public String getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(String performedBy) {
        this.performedBy = performedBy;
    }
    
    public Object getData() {
        return data;
    }
    
    public void setData(Object data) {
        this.data = data;
    }
    
    public String getErrorCode() {
        return errorCode;
    }
    
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    public Boolean getRequiresFollowUp() {
        return requiresFollowUp;
    }
    
    public void setRequiresFollowUp(Boolean requiresFollowUp) {
        this.requiresFollowUp = requiresFollowUp;
    }
    
    public String getFollowUpAction() {
        return followUpAction;
    }
    
    public void setFollowUpAction(String followUpAction) {
        this.followUpAction = followUpAction;
    }
}
