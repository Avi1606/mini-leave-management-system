package com.avi.leavemgmt.service;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class LeaveCalculationServiceTest {
    
    private final LeaveCalculationService leaveCalculationService = new LeaveCalculationService();
    
    @Test
    void testCalculateProRatedLeaveForFullYear() {
        // Employee joining on January 1st should get full allocation
        LocalDate joiningDate = LocalDate.of(2025, 1, 1);
        int proRatedLeave = leaveCalculationService.calculateProRatedLeaveForEmployee(joiningDate);
        assertEquals(20, proRatedLeave);
    }
    
    @Test
    void testCalculateProRatedLeaveForMidYear() {
        // Employee joining on July 1st (mid-year) should get roughly half allocation
        LocalDate joiningDate = LocalDate.of(2025, 7, 1);
        int proRatedLeave = leaveCalculationService.calculateProRatedLeaveForEmployee(joiningDate);
        assertTrue(proRatedLeave >= 10 && proRatedLeave <= 12, 
                "Pro-rated leave for July joiner should be around 10-12 days, got: " + proRatedLeave);
    }
    
    @Test
    void testCalculateProRatedLeaveForLateJoiner() {
        // Employee joining on December 1st should get minimal allocation
        LocalDate joiningDate = LocalDate.of(2025, 12, 1);
        int proRatedLeave = leaveCalculationService.calculateProRatedLeaveForEmployee(joiningDate);
        assertTrue(proRatedLeave >= 1 && proRatedLeave <= 3, 
                "Pro-rated leave for December joiner should be 1-3 days, got: " + proRatedLeave);
    }
    
    @Test
    void testIsEligibleForLeave() {
        LocalDate joiningDate = LocalDate.of(2025, 1, 15);
        
        // Should be eligible for leave after joining date
        assertTrue(leaveCalculationService.isEligibleForLeave(joiningDate, LocalDate.of(2025, 1, 16)));
        
        // Should not be eligible for leave before joining date
        assertFalse(leaveCalculationService.isEligibleForLeave(joiningDate, LocalDate.of(2025, 1, 14)));
        
        // Should be eligible on joining date
        assertTrue(leaveCalculationService.isEligibleForLeave(joiningDate, joiningDate));
    }
}