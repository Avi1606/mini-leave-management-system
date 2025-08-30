package com.avi.leavemgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

/**
 * Entity for auditing leave request changes
 */
@Entity
@Table(name = "leave_audit", indexes = {
    @Index(name = "idx_leave_audit_request_id", columnList = "leave_request_id"),
    @Index(name = "idx_leave_audit_timestamp", columnList = "action_timestamp")
})
public class LeaveAudit {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull
    @Column(name = "leave_request_id", nullable = false)
    private Long leaveRequestId;
    
    @Enumerated(EnumType.STRING)
    @NotNull
    @Column(name = "action", nullable = false)
    private AuditAction action;
    
    @Column(name = "performed_by")
    private Long performedBy;
    
    @Column(name = "performed_by_name")
    private String performedByName;
    
    @NotNull
    @Column(name = "action_timestamp", nullable = false)
    private LocalDateTime actionTimestamp;
    
    @Column(name = "old_status")
    @Enumerated(EnumType.STRING)
    private LeaveRequest.LeaveStatus oldStatus;
    
    @Column(name = "new_status")
    @Enumerated(EnumType.STRING)
    private LeaveRequest.LeaveStatus newStatus;
    
    @Column(name = "comments", length = 500)
    private String comments;
    
    @Column(name = "details", length = 1000)
    private String details;
    
    public enum AuditAction {
        CREATED, SUBMITTED, APPROVED, REJECTED, CANCELLED, UPDATED
    }
    
    // Constructors
    public LeaveAudit() {
        this.actionTimestamp = LocalDateTime.now();
    }
    
    public LeaveAudit(Long leaveRequestId, AuditAction action, Long performedBy, String performedByName) {
        this();
        this.leaveRequestId = leaveRequestId;
        this.action = action;
        this.performedBy = performedBy;
        this.performedByName = performedByName;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getLeaveRequestId() {
        return leaveRequestId;
    }
    
    public void setLeaveRequestId(Long leaveRequestId) {
        this.leaveRequestId = leaveRequestId;
    }
    
    public AuditAction getAction() {
        return action;
    }
    
    public void setAction(AuditAction action) {
        this.action = action;
    }
    
    public Long getPerformedBy() {
        return performedBy;
    }
    
    public void setPerformedBy(Long performedBy) {
        this.performedBy = performedBy;
    }
    
    public String getPerformedByName() {
        return performedByName;
    }
    
    public void setPerformedByName(String performedByName) {
        this.performedByName = performedByName;
    }
    
    public LocalDateTime getActionTimestamp() {
        return actionTimestamp;
    }
    
    public void setActionTimestamp(LocalDateTime actionTimestamp) {
        this.actionTimestamp = actionTimestamp;
    }
    
    public LeaveRequest.LeaveStatus getOldStatus() {
        return oldStatus;
    }
    
    public void setOldStatus(LeaveRequest.LeaveStatus oldStatus) {
        this.oldStatus = oldStatus;
    }
    
    public LeaveRequest.LeaveStatus getNewStatus() {
        return newStatus;
    }
    
    public void setNewStatus(LeaveRequest.LeaveStatus newStatus) {
        this.newStatus = newStatus;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    public String getDetails() {
        return details;
    }
    
    public void setDetails(String details) {
        this.details = details;
    }
    
    @Override
    public String toString() {
        return "LeaveAudit{" +
                "id=" + id +
                ", leaveRequestId=" + leaveRequestId +
                ", action=" + action +
                ", performedBy=" + performedBy +
                ", actionTimestamp=" + actionTimestamp +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                '}';
    }
}