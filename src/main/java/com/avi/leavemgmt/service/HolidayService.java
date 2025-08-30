package com.avi.leavemgmt.service;

import com.avi.leavemgmt.model.Holiday;
import com.avi.leavemgmt.repository.HolidayRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

/**
 * Service for managing holidays and working days calculation
 */
@Service
@Transactional
public class HolidayService {
    
    private final HolidayRepository holidayRepository;
    
    @Autowired
    public HolidayService(HolidayRepository holidayRepository) {
        this.holidayRepository = holidayRepository;
    }
    
    /**
     * Calculate working days excluding weekends and public holidays
     */
    public long calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        if (startDate.isAfter(endDate)) {
            return 0;
        }
        
        long workingDays = 0;
        LocalDate currentDate = startDate;
        
        // Get holidays in the range
        List<Holiday> holidays = holidayRepository.findHolidaysInRange(startDate, endDate);
        
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            
            // Skip weekends
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                // Check if it's not a holiday
                final LocalDate dateToCheck = currentDate;
                boolean isHoliday = holidays.stream()
                    .anyMatch(holiday -> holiday.getDate().equals(dateToCheck));
                
                if (!isHoliday) {
                    workingDays++;
                }
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return workingDays;
    }
    
    /**
     * Check if a date is a holiday
     */
    public boolean isHoliday(LocalDate date) {
        return holidayRepository.existsByDate(date);
    }
    
    /**
     * Get all holidays in a date range
     */
    public List<Holiday> getHolidaysInRange(LocalDate startDate, LocalDate endDate) {
        return holidayRepository.findHolidaysInRange(startDate, endDate);
    }
    
    /**
     * Get all holidays
     */
    public List<Holiday> getAllHolidays() {
        return holidayRepository.findAll();
    }
    
    /**
     * Add a new holiday
     */
    public Holiday addHoliday(Holiday holiday) {
        return holidayRepository.save(holiday);
    }
    
    /**
     * Delete a holiday
     */
    public void deleteHoliday(Long id) {
        holidayRepository.deleteById(id);
    }
}