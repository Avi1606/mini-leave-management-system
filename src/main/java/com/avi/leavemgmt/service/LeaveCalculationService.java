package com.avi.leavemgmt.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

/**
 * Service for calculating leave balance based on various policies
 */
@Service
public class LeaveCalculationService {
    
    // Standard annual leave allocation
    private static final int STANDARD_ANNUAL_LEAVE = 20;
    private static final int DAYS_IN_YEAR = 365;
    
    /**
     * Calculate pro-rated annual leave for mid-year joiners
     */
    public int calculateProRatedLeave(LocalDate joiningDate, int currentYear) {
        LocalDate yearStart = LocalDate.of(currentYear, 1, 1);
        LocalDate yearEnd = LocalDate.of(currentYear, 12, 31);
        
        // If joined before the year, give full allocation
        if (joiningDate.isBefore(yearStart) || joiningDate.isEqual(yearStart)) {
            return STANDARD_ANNUAL_LEAVE;
        }
        
        // If joining date is in the future or after year end, no allocation
        if (joiningDate.isAfter(yearEnd)) {
            return 0;
        }
        
        // Calculate remaining days in the year from joining date
        long remainingDaysInYear = ChronoUnit.DAYS.between(joiningDate, yearEnd) + 1;
        
        // Pro-rate the leave allocation
        double proRatedLeave = (remainingDaysInYear * STANDARD_ANNUAL_LEAVE) / (double) DAYS_IN_YEAR;
        
        // Round up to nearest integer (employee benefit)
        return (int) Math.ceil(proRatedLeave);
    }
    
    /**
     * Calculate pro-rated leave for current year based on joining date
     */
    public int calculateProRatedLeaveForEmployee(LocalDate joiningDate) {
        int currentYear = LocalDate.now().getYear();
        // If joining date is in a different year, calculate for the joining year initially
        if (joiningDate.getYear() != currentYear) {
            // For employees who joined in previous years, give full allocation for current year
            return STANDARD_ANNUAL_LEAVE;
        }
        return calculateProRatedLeave(joiningDate, currentYear);
    }
    
    /**
     * Update leave balance for new year
     */
    public int calculateNewYearLeaveBalance(LocalDate joiningDate, int year) {
        LocalDate yearStart = LocalDate.of(year, 1, 1);
        
        // If joined before or at the start of the year, give full allocation
        if (joiningDate.isBefore(yearStart) || joiningDate.isEqual(yearStart)) {
            return STANDARD_ANNUAL_LEAVE;
        } else {
            // Pro-rate for mid-year joiners
            return calculateProRatedLeave(joiningDate, year);
        }
    }
    
    /**
     * Check if employee is eligible for leave based on joining date
     */
    public boolean isEligibleForLeave(LocalDate joiningDate, LocalDate leaveStartDate) {
        // Employee can apply for leave only after joining
        return !leaveStartDate.isBefore(joiningDate);
    }
    
    /**
     * Calculate leave balance adjustment needed for an employee
     */
    public int calculateLeaveBalanceAdjustment(LocalDate joiningDate, int currentBalance) {
        int correctBalance = calculateProRatedLeaveForEmployee(joiningDate);
        return correctBalance - currentBalance;
    }
}