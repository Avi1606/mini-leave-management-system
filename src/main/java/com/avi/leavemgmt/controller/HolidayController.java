package com.avi.leavemgmt.controller;

import com.avi.leavemgmt.model.Holiday;
import com.avi.leavemgmt.service.HolidayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/holidays")
@Tag(name = "Holiday Management", description = "APIs for managing public holidays")
public class HolidayController {
    
    private final HolidayService holidayService;
    
    @Autowired
    public HolidayController(HolidayService holidayService) {
        this.holidayService = holidayService;
    }
    
    @GetMapping
    @Operation(summary = "Get all holidays", description = "Retrieve a list of all public holidays")
    public ResponseEntity<List<Holiday>> getAllHolidays() {
        List<Holiday> holidays = holidayService.getAllHolidays();
        return ResponseEntity.ok(holidays);
    }
    
    @GetMapping("/range")
    @Operation(summary = "Get holidays in date range", description = "Retrieve holidays within a specific date range")
    public ResponseEntity<List<Holiday>> getHolidaysInRange(
            @Parameter(description = "Start date (YYYY-MM-DD)", required = true) 
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        List<Holiday> holidays = holidayService.getHolidaysInRange(startDate, endDate);
        return ResponseEntity.ok(holidays);
    }
    
    @GetMapping("/working-days")
    @Operation(summary = "Calculate working days", description = "Calculate working days between two dates excluding weekends and holidays")
    public ResponseEntity<Map<String, Object>> calculateWorkingDays(
            @Parameter(description = "Start date (YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @Parameter(description = "End date (YYYY-MM-DD)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        
        long workingDays = holidayService.calculateWorkingDays(startDate, endDate);
        List<Holiday> holidays = holidayService.getHolidaysInRange(startDate, endDate);
        
        return ResponseEntity.ok(Map.of(
                "startDate", startDate,
                "endDate", endDate,
                "workingDays", workingDays,
                "holidaysInRange", holidays
        ));
    }
    
    @PostMapping
    @Operation(summary = "Add new holiday", description = "Add a new public holiday")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Holiday created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Holiday> addHoliday(
            @Parameter(description = "Holiday data", required = true) @Valid @RequestBody Holiday holiday) {
        try {
            Holiday savedHoliday = holidayService.addHoliday(holiday);
            return ResponseEntity.status(HttpStatus.CREATED).body(savedHoliday);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete holiday", description = "Delete a public holiday")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Holiday deleted successfully"),
            @ApiResponse(responseCode = "404", description = "Holiday not found")
    })
    public ResponseEntity<Void> deleteHoliday(
            @Parameter(description = "Holiday ID", required = true) @PathVariable Long id) {
        try {
            holidayService.deleteHoliday(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}