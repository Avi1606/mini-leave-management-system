package com.avi.leavemgmt.controller;

import com.avi.leavemgmt.dto.LeaveRequestDTO;
import com.avi.leavemgmt.service.LeaveRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/leave-requests")
@Tag(name = "Leave Request Management", description = "APIs for managing leave requests")
public class LeaveRequestController {
    
    private final LeaveRequestService leaveRequestService;
    
    @Autowired
    public LeaveRequestController(LeaveRequestService leaveRequestService) {
        this.leaveRequestService = leaveRequestService;
    }
    
    @GetMapping
    @Operation(summary = "Get all leave requests", description = "Retrieve a list of all leave requests")
    @ApiResponse(responseCode = "200", description = "Successfully retrieved leave requests")
    public ResponseEntity<List<LeaveRequestDTO>> getAllLeaveRequests() {
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.getAllLeaveRequests();
        return ResponseEntity.ok(leaveRequests);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get leave request by ID", description = "Retrieve a specific leave request by its ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request found"),
            @ApiResponse(responseCode = "404", description = "Leave request not found")
    })
    public ResponseEntity<LeaveRequestDTO> getLeaveRequestById(
            @Parameter(description = "Leave request ID", required = true) @PathVariable Long id) {
        Optional<LeaveRequestDTO> leaveRequest = leaveRequestService.getLeaveRequestById(id);
        return leaveRequest.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/employee/{employeeId}")
    @Operation(summary = "Get leave requests by employee", description = "Retrieve leave requests for a specific employee")
    public ResponseEntity<List<LeaveRequestDTO>> getLeaveRequestsByEmployee(
            @Parameter(description = "Employee ID", required = true) @PathVariable Long employeeId) {
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.getLeaveRequestsByEmployeeId(employeeId);
        return ResponseEntity.ok(leaveRequests);
    }
    
    @PostMapping
    @Operation(summary = "Submit leave request", description = "Submit a new leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Leave request submitted successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid input data or business rule violation"),
            @ApiResponse(responseCode = "404", description = "Employee not found")
    })
    public ResponseEntity<?> submitLeaveRequest(
            @Parameter(description = "Leave request data", required = true) @Valid @RequestBody LeaveRequestDTO leaveRequestDTO) {
        try {
            LeaveRequestDTO createdRequest = leaveRequestService.submitLeaveRequest(leaveRequestDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/approve")
    @Operation(summary = "Approve leave request", description = "Approve a pending leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request approved successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or business rule violation"),
            @ApiResponse(responseCode = "404", description = "Leave request not found"),
            @ApiResponse(responseCode = "403", description = "Manager does not have authority to approve")
    })
    public ResponseEntity<?> approveLeaveRequest(
            @Parameter(description = "Leave request ID", required = true) @PathVariable Long id,
            @Parameter(description = "Approval data", required = true) @RequestBody Map<String, Object> approvalData) {
        try {
            Long managerId = Long.valueOf(approvalData.get("managerId").toString());
            String comments = (String) approvalData.getOrDefault("comments", "");
            
            LeaveRequestDTO approvedRequest = leaveRequestService.approveLeaveRequest(id, managerId, comments);
            return ResponseEntity.ok(approvedRequest);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("authority")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @PutMapping("/{id}/reject")
    @Operation(summary = "Reject leave request", description = "Reject a pending leave request")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request rejected successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or business rule violation"),
            @ApiResponse(responseCode = "404", description = "Leave request not found"),
            @ApiResponse(responseCode = "403", description = "Manager does not have authority to reject")
    })
    public ResponseEntity<?> rejectLeaveRequest(
            @Parameter(description = "Leave request ID", required = true) @PathVariable Long id,
            @Parameter(description = "Rejection data", required = true) @RequestBody Map<String, Object> rejectionData) {
        try {
            Long managerId = Long.valueOf(rejectionData.get("managerId").toString());
            String comments = (String) rejectionData.getOrDefault("comments", "");
            
            LeaveRequestDTO rejectedRequest = leaveRequestService.rejectLeaveRequest(id, managerId, comments);
            return ResponseEntity.ok(rejectedRequest);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("authority")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/manager/{managerId}")
    @Operation(summary = "Get leave requests for manager", description = "Retrieve leave requests for team members of a specific manager")
    public ResponseEntity<List<LeaveRequestDTO>> getLeaveRequestsForManager(
            @Parameter(description = "Manager ID", required = true) @PathVariable Long managerId) {
        List<LeaveRequestDTO> leaveRequests = leaveRequestService.getLeaveRequestsForManager(managerId);
        return ResponseEntity.ok(leaveRequests);
    }
    
    @PutMapping("/{id}/cancel")
    @Operation(summary = "Cancel leave request", description = "Cancel a pending leave request (only by the employee who submitted it)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Leave request cancelled successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request or business rule violation"),
            @ApiResponse(responseCode = "404", description = "Leave request not found"),
            @ApiResponse(responseCode = "403", description = "Employee does not have authority to cancel")
    })
    public ResponseEntity<?> cancelLeaveRequest(
            @Parameter(description = "Leave request ID", required = true) @PathVariable Long id,
            @Parameter(description = "Cancellation data", required = true) @RequestBody Map<String, Object> cancellationData) {
        try {
            Long employeeId = Long.valueOf(cancellationData.get("employeeId").toString());
            String reason = (String) cancellationData.getOrDefault("reason", "Cancelled by employee");
            
            LeaveRequestDTO cancelledRequest = leaveRequestService.cancelLeaveRequest(id, employeeId, reason);
            return ResponseEntity.ok(cancelledRequest);
        } catch (RuntimeException e) {
            if (e.getMessage().contains("not found")) {
                return ResponseEntity.notFound().build();
            } else if (e.getMessage().contains("authority") || e.getMessage().contains("authorized")) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body(Map.of("error", e.getMessage()));
            }
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
}