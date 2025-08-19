package com.avi.leavemgmt.dto;

import com.avi.leavemgmt.model.LeaveRequest;
import com.avi.leavemgmt.model.LeaveType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

public class LeaveRequestDTO {
    
    private Long id;
    
    @NotNull(message = "Employee ID is required")
    private Long employeeId;
    private String employeeName;
    
    @NotNull(message = "Leave type is required")
    private LeaveType leaveType;
    
    @NotNull(message = "Start date is required")
    private LocalDate startDate;
    
    @NotNull(message = "End date is required")
    private LocalDate endDate;
    
    @NotBlank(message = "Reason is required")
    @Size(min = 10, max = 500, message = "Reason must be between 10 and 500 characters")
    private String reason;
    
    private LeaveRequest.LeaveStatus status;
    private LocalDate appliedDate;
    private Long approvedBy;
    private String approvedByName;
    private LocalDate approvedDate;
    
    @Size(max = 255, message = "Comments cannot exceed 255 characters")
    private String comments;
    
    private Long workingDays;
    
    // Constructors
    public LeaveRequestDTO() {
    }
    
    public LeaveRequestDTO(Long id, Long employeeId, LeaveType leaveType, LocalDate startDate,
                          LocalDate endDate, String reason, LeaveRequest.LeaveStatus status) {
        this.id = id;
        this.employeeId = employeeId;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.reason = reason;
        this.status = status;
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
    
    public String getEmployeeName() {
        return employeeName;
    }
    
    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
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
    
    public LeaveRequest.LeaveStatus getStatus() {
        return status;
    }
    
    public void setStatus(LeaveRequest.LeaveStatus status) {
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
    
    public String getApprovedByName() {
        return approvedByName;
    }
    
    public void setApprovedByName(String approvedByName) {
        this.approvedByName = approvedByName;
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
    
    public Long getWorkingDays() {
        return workingDays;
    }
    
    public void setWorkingDays(Long workingDays) {
        this.workingDays = workingDays;
    }
}