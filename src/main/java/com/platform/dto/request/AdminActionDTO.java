package com.platform.dto.request;

import java.util.Map;

/**
 * Data Transfer Object for admin actions
 */
public class AdminActionDTO {
    private String actionType; // VERIFY, ACTIVATE, DEACTIVATE, APPROVE, REJECT, RESOLVE
    private Long targetUserId;
    private String reason;
    private String notes;
    private String actionBy; // Admin identifier
    private Long timestamp;
    private Boolean requiresNotification;
    private String notificationMessage;
    private String priority; // HIGH, MEDIUM, LOW
    private Map<String, Object> additionalData;
    
    public AdminActionDTO() {
        // Default constructor
    }
    
    public AdminActionDTO(String actionType, Long targetUserId, String reason, String actionBy) {
        this.actionType = actionType;
        this.targetUserId = targetUserId;
        this.reason = reason;
        this.actionBy = actionBy;
    }
    
    public String getActionType() {
        return actionType;
    }
    
    public void setActionType(String actionType) {
        this.actionType = actionType;
    }
    
    public Long getTargetUserId() {
        return targetUserId;
    }
    
    public void setTargetUserId(Long targetUserId) {
        this.targetUserId = targetUserId;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public String getNotes() {
        return notes;
    }
    
    public void setNotes(String notes) {
        this.notes = notes;
    }
    
    public String getActionBy() {
        return actionBy;
    }
    
    public void setActionBy(String actionBy) {
        this.actionBy = actionBy;
    }
    
    public Long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }
    
    public Boolean getRequiresNotification() {
        return requiresNotification;
    }
    
    public void setRequiresNotification(Boolean requiresNotification) {
        this.requiresNotification = requiresNotification;
    }
    
    public String getNotificationMessage() {
        return notificationMessage;
    }
    
    public void setNotificationMessage(String notificationMessage) {
        this.notificationMessage = notificationMessage;
    }
    
    public String getPriority() {
        return priority;
    }
    
    public void setPriority(String priority) {
        this.priority = priority;
    }
    
    public Map<String, Object> getAdditionalData() {
        return additionalData;
    }
    
    public void setAdditionalData(Map<String, Object> additionalData) {
        this.additionalData = additionalData;
    }
}
