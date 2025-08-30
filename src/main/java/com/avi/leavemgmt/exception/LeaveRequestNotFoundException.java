package com.avi.leavemgmt.exception;

/**
 * Exception thrown when leave request is not found
 */
public class LeaveRequestNotFoundException extends LeaveManagementException {
    
    public LeaveRequestNotFoundException(Long requestId) {
        super("Leave request not found with id: " + requestId);
    }
    
    public LeaveRequestNotFoundException(String message) {
        super(message);
    }
}