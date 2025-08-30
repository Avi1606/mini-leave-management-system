package com.avi.leavemgmt.service;

import com.avi.leavemgmt.model.Holiday;
import com.avi.leavemgmt.repository.HolidayRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HolidayServiceTest {
    
    @Mock
    private HolidayRepository holidayRepository;
    
    @InjectMocks
    private HolidayService holidayService;
    
    @Test
    void testCalculateWorkingDaysWithoutHolidays() {
        // Mock no holidays in the range
        when(holidayRepository.findHolidaysInRange(any(), any())).thenReturn(Collections.emptyList());
        
        // Monday to Friday (5 working days)
        LocalDate start = LocalDate.of(2025, 1, 6); // Monday
        LocalDate end = LocalDate.of(2025, 1, 10);   // Friday
        
        long workingDays = holidayService.calculateWorkingDays(start, end);
        assertEquals(5, workingDays);
    }
    
    @Test
    void testCalculateWorkingDaysWithWeekends() {
        // Mock no holidays in the range
        when(holidayRepository.findHolidaysInRange(any(), any())).thenReturn(Collections.emptyList());
        
        // Monday to Sunday (5 working days, excluding Saturday and Sunday)
        LocalDate start = LocalDate.of(2025, 1, 6); // Monday
        LocalDate end = LocalDate.of(2025, 1, 12);   // Sunday
        
        long workingDays = holidayService.calculateWorkingDays(start, end);
        assertEquals(5, workingDays);
    }
    
    @Test
    void testCalculateWorkingDaysWithHolidays() {
        // Mock one holiday in the range
        Holiday holiday = new Holiday("New Year", LocalDate.of(2025, 1, 6), "Holiday", false);
        List<Holiday> holidays = Arrays.asList(holiday);
        when(holidayRepository.findHolidaysInRange(any(), any())).thenReturn(holidays);
        
        // Monday (holiday) to Friday (4 working days)
        LocalDate start = LocalDate.of(2025, 1, 6); // Monday (Holiday)
        LocalDate end = LocalDate.of(2025, 1, 10);   // Friday
        
        long workingDays = holidayService.calculateWorkingDays(start, end);
        assertEquals(4, workingDays);
    }
    
    @Test
    void testCalculateWorkingDaysInvalidRange() {
        // End date before start date should return 0
        LocalDate start = LocalDate.of(2025, 1, 10);
        LocalDate end = LocalDate.of(2025, 1, 6);
        
        long workingDays = holidayService.calculateWorkingDays(start, end);
        assertEquals(0, workingDays);
    }
    
    @Test
    void testCalculateWorkingDaysSameDay() {
        // Mock no holidays
        when(holidayRepository.findHolidaysInRange(any(), any())).thenReturn(Collections.emptyList());
        
        // Same day (Monday)
        LocalDate date = LocalDate.of(2025, 1, 6); // Monday
        
        long workingDays = holidayService.calculateWorkingDays(date, date);
        assertEquals(1, workingDays);
    }
    
    @Test
    void testCalculateWorkingDaysSameDayWeekend() {
        // Mock no holidays
        when(holidayRepository.findHolidaysInRange(any(), any())).thenReturn(Collections.emptyList());
        
        // Same day (Saturday)
        LocalDate date = LocalDate.of(2025, 1, 11); // Saturday
        
        long workingDays = holidayService.calculateWorkingDays(date, date);
        assertEquals(0, workingDays);
    }
}