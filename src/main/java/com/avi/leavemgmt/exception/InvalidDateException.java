package com.avi.leavemgmt.exception;

/**
 * Exception thrown for invalid date-related operations
 */
public class InvalidDateException extends LeaveManagementException {
    
    public InvalidDateException(String message) {
        super(message);
    }
}