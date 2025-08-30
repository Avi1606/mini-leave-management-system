package com.avi.leavemgmt.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

/**
 * Entity representing a public holiday
 */
@Entity
@Table(name = "holidays", indexes = {
    @Index(name = "idx_holiday_date", columnList = "holiday_date")
})
public class Holiday {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @NotBlank(message = "Holiday name is required")
    @Size(min = 2, max = 100, message = "Holiday name must be between 2 and 100 characters")
    @Column(name = "name", nullable = false, length = 100)
    private String name;
    
    @NotNull(message = "Holiday date is required")
    @Column(name = "holiday_date", nullable = false, unique = true)
    private LocalDate date;
    
    @Size(max = 255, message = "Description cannot exceed 255 characters")
    @Column(name = "description")
    private String description;
    
    @Column(name = "recurring", nullable = false)
    private boolean recurring = false;
    
    // Constructors
    public Holiday() {
    }
    
    public Holiday(String name, LocalDate date, String description, boolean recurring) {
        this.name = name;
        this.date = date;
        this.description = description;
        this.recurring = recurring;
    }
    
    // Getters and Setters
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public LocalDate getDate() {
        return date;
    }
    
    public void setDate(LocalDate date) {
        this.date = date;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public boolean isRecurring() {
        return recurring;
    }
    
    public void setRecurring(boolean recurring) {
        this.recurring = recurring;
    }
    
    @Override
    public String toString() {
        return "Holiday{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", date=" + date +
                ", description='" + description + '\'' +
                ", recurring=" + recurring +
                '}';
    }
}