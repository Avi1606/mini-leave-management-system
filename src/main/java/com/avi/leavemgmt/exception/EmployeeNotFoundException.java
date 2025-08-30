package com.avi.leavemgmt.exception;

/**
 * Exception thrown when employee is not found
 */
public class EmployeeNotFoundException extends LeaveManagementException {
    
    public EmployeeNotFoundException(Long employeeId) {
        super("Employee not found with id: " + employeeId);
    }
    
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}