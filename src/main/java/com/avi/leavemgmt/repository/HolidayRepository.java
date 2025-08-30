package com.avi.leavemgmt.repository;

import com.avi.leavemgmt.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    
    List<Holiday> findByDateBetween(LocalDate startDate, LocalDate endDate);
    
    boolean existsByDate(LocalDate date);
    
    @Query("SELECT h FROM Holiday h WHERE h.date >= :startDate AND h.date <= :endDate ORDER BY h.date")
    List<Holiday> findHolidaysInRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
    
    @Query("SELECT h FROM Holiday h WHERE h.recurring = true")
    List<Holiday> findRecurringHolidays();
    
    @Query("SELECT COUNT(h) FROM Holiday h WHERE h.date >= :startDate AND h.date <= :endDate")
    long countHolidaysInRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}