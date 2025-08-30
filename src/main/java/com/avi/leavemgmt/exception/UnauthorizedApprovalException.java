package com.avi.leavemgmt.exception;

/**
 * Exception thrown when manager is not authorized to approve/reject leave request
 */
public class UnauthorizedApprovalException extends LeaveManagementException {
    
    public UnauthorizedApprovalException(Long managerId, Long employeeId) {
        super("Manager " + managerId + " is not authorized to approve leave requests for employee " + employeeId);
    }
    
    public UnauthorizedApprovalException(String message) {
        super(message);
    }
}