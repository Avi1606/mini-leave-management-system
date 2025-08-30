package com.avi.leavemgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Entity
@Table(name = "leave_requests")
public class LeaveRequest {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotNull(message = "Employee ID is required")
    @Column(name = "employee_id", nullable = false)
    private Long employeeId;
    
    @Enumerated(EnumType.STRING)
    @NotNull(message = "Leave type is required")
    @Column(name = "leave_type", nullable = false)
    private LeaveType leaveType;
    
    @NotNull(message = "Start date is required")
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;
    
    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    @Column(name = "reason", nullable = false, length = 500)
    private String reason;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private LeaveStatus status = LeaveStatus.PENDING;
    
    @Column(name = "applied_date", nullable = false)
    private LocalDate appliedDate;
    
    @Column(name = "approved_by")
    private Long approvedBy;
    
    @Column(name = "approved_date")
    private LocalDate approvedDate;
    
    @Size(max = 255, message = "Comments cannot exceed 255 characters")
    @Column(name = "comments")
    private String comments;
    
    // Nested enum for status
    public enum LeaveStatus {
        PENDING, APPROVED, REJECTED, CANCELLED
    }
    
    // Constructors
    public LeaveRequest() {
        this.appliedDate = LocalDate.now();
    }
    
    public LeaveRequest(Long employeeId, LeaveType leaveType, LocalDate startDate, 
                      LocalDate endDate, String reason) {
        this();
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public Long getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }
    
    public LeaveType getLeaveType() {
        return leaveType;
    }
    
    public void setLeaveType(LeaveType leaveType) {
        this.leaveType = leaveType;
    }
    
    public LocalDate getStartDate() {
        return startDate;
    }
    
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }
    
    public LocalDate getEndDate() {
        return endDate;
    }
    
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public LeaveStatus getStatus() {
        return status;
    }
    
    public void setStatus(LeaveStatus status) {
        this.status = status;
    }
    
    public LocalDate getAppliedDate() {
        return appliedDate;
    }
    
    public void setAppliedDate(LocalDate appliedDate) {
        this.appliedDate = appliedDate;
    }
    
    public Long getApprovedBy() {
        return approvedBy;
    }
    
    public void setApprovedBy(Long approvedBy) {
        this.approvedBy = approvedBy;
    }
    
    public LocalDate getApprovedDate() {
        return approvedDate;
    }
    
    public void setApprovedDate(LocalDate approvedDate) {
        this.approvedDate = approvedDate;
    }
    
    public String getComments() {
        return comments;
    }
    
    public void setComments(String comments) {
        this.comments = comments;
    }
    
    @Override
    public String toString() {
        return "LeaveRequest{" +
                "id=" + id +
                ", employeeId=" + employeeId +
                ", leaveType=" + leaveType +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", reason='" + reason + '\'' +
                ", status=" + status +
                ", appliedDate=" + appliedDate +
                ", approvedBy=" + approvedBy +
                ", approvedDate=" + approvedDate +
                ", comments='" + comments + '\'' +
                '}';
    }
}