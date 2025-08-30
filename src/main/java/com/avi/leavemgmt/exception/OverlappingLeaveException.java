package com.avi.leavemgmt.exception;

/**
 * Exception thrown when leave requests overlap
 */
public class OverlappingLeaveException extends LeaveManagementException {
    
    public OverlappingLeaveException() {
        super("Leave request overlaps with existing approved or pending leave");
    }
    
    public OverlappingLeaveException(String message) {
        super(message);
    }
}