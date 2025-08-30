package com.avi.leavemgmt.exception;

/**
 * Exception thrown when employee has insufficient leave balance
 */
public class InsufficientLeaveBalanceException extends LeaveManagementException {
    
    public InsufficientLeaveBalanceException(int requested, int available) {
        super("Requested leave days (" + requested + ") exceed available balance (" + available + ")");
    }
    
    public InsufficientLeaveBalanceException(String message) {
        super(message);
    }
}