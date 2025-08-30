package com.avi.leavemgmt.controller;

import com.avi.leavemgmt.model.LeaveAudit;
import com.avi.leavemgmt.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit")
@Tag(name = "Audit Trail", description = "APIs for accessing audit trail information")
public class AuditController {
    
    private final AuditService auditService;
    
    @Autowired
    public AuditController(AuditService auditService) {
        this.auditService = auditService;
    }
    
    @GetMapping("/leave-request/{leaveRequestId}")
    @Operation(summary = "Get audit trail for leave request", description = "Retrieve audit trail for a specific leave request")
    public ResponseEntity<List<LeaveAudit>> getLeaveRequestAuditTrail(
            @Parameter(description = "Leave request ID", required = true) @PathVariable Long leaveRequestId) {
        
        List<LeaveAudit> auditTrail = auditService.getLeaveRequestAuditTrail(leaveRequestId);
        return ResponseEntity.ok(auditTrail);
    }
    
    @GetMapping("/user/{userId}")
    @Operation(summary = "Get user audit history", description = "Retrieve audit history for a specific user")
    public ResponseEntity<List<LeaveAudit>> getUserAuditHistory(
            @Parameter(description = "User ID", required = true) @PathVariable Long userId) {
        
        List<LeaveAudit> auditHistory = auditService.getUserAuditHistory(userId);
        return ResponseEntity.ok(auditHistory);
    }
    
    @GetMapping("/action/{action}")
    @Operation(summary = "Get audit by action type", description = "Retrieve audit entries by action type")
    public ResponseEntity<List<LeaveAudit>> getAuditByAction(
            @Parameter(description = "Action type", required = true) @PathVariable LeaveAudit.AuditAction action) {
        
        List<LeaveAudit> auditEntries = auditService.getAuditByAction(action);
        return ResponseEntity.ok(auditEntries);
    }
    
    @GetMapping("/date-range")
    @Operation(summary = "Get audit in date range", description = "Retrieve audit entries within a specific date range")
    public ResponseEntity<List<LeaveAudit>> getAuditInDateRange(
            @Parameter(description = "Start date time (YYYY-MM-DDTHH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @Parameter(description = "End date time (YYYY-MM-DDTHH:mm:ss)", required = true)
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        
        List<LeaveAudit> auditEntries = auditService.getAuditInDateRange(startTime, endTime);
        return ResponseEntity.ok(auditEntries);
    }
}