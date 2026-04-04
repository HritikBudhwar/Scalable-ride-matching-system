package com.platform.dto.request;

public class AdminActionDTO {

    private Long targetId;      // userId or driverId
    private String action;      // APPROVE | REJECT | SUSPEND | REINSTATE | REFUND
    private String reason;
    private boolean lifetimeBan;

    public Long getTargetId() { return targetId; }
    public void setTargetId(Long targetId) { this.targetId = targetId; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }

    public boolean isLifetimeBan() { return lifetimeBan; }
    public void setLifetimeBan(boolean lifetimeBan) { this.lifetimeBan = lifetimeBan; }
}