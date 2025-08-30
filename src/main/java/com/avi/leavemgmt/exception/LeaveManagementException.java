package com.avi.leavemgmt.exception;

/**
 * Base exception for all leave management system exceptions
 */
public class LeaveManagementException extends RuntimeException {
    
    public LeaveManagementException(String message) {
        super(message);
    }
    
    public LeaveManagementException(String message, Throwable cause) {
        super(message, cause);
    }
}