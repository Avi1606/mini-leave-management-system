package com.avi.leavemgmt.exception;

/**
 * Exception thrown when leave request status is invalid for the operation
 */
public class InvalidLeaveStatusException extends LeaveManagementException {
    
    public InvalidLeaveStatusException(String currentStatus, String requiredStatus) {
        super("Leave request is in '" + currentStatus + "' status, required: '" + requiredStatus + "'");
    }
    
    public InvalidLeaveStatusException(String message) {
        super(message);
    }
}