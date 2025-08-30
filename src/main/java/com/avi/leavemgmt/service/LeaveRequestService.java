package com.avi.leavemgmt.service;

import com.avi.leavemgmt.dto.LeaveRequestDTO;
import com.avi.leavemgmt.exception.*;
import com.avi.leavemgmt.model.Employee;
import com.avi.leavemgmt.model.LeaveAudit;
import com.avi.leavemgmt.model.LeaveRequest;
import com.avi.leavemgmt.repository.EmployeeRepository;
import com.avi.leavemgmt.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeaveRequestService {
    
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    private final HolidayService holidayService;
    private final AuditService auditService;
    
    @Autowired
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, 
                              EmployeeRepository employeeRepository,
                              HolidayService holidayService,
                              AuditService auditService) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
        this.holidayService = holidayService;
        this.auditService = auditService;
    }
    
    public List<LeaveRequestDTO> getAllLeaveRequests() {
        return leaveRequestRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<LeaveRequestDTO> getLeaveRequestById(Long id) {
        return leaveRequestRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public List<LeaveRequestDTO> getLeaveRequestsByEmployeeId(Long employeeId) {
        return leaveRequestRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public LeaveRequestDTO submitLeaveRequest(LeaveRequestDTO leaveRequestDTO) {
        // Validate employee exists
        Employee employee = employeeRepository.findById(leaveRequestDTO.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(leaveRequestDTO.getEmployeeId()));
        
        // Validate dates
        if (leaveRequestDTO.getStartDate().isAfter(leaveRequestDTO.getEndDate())) {
            throw new InvalidDateException("Start date cannot be after end date");
        }
        
        if (leaveRequestDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new InvalidDateException("Cannot apply for leave in the past");
        }

        // Validate start date must not be before joining date
        if (leaveRequestDTO.getStartDate().isBefore(employee.getJoiningDate())) {
            throw new InvalidDateException("Cannot apply for leave before joining date");
        }
        
        // Check for overlapping approved leaves
        List<LeaveRequest> overlappingLeaves = leaveRequestRepository.findOverlappingApprovedLeaves(
                leaveRequestDTO.getEmployeeId(),
                leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate()
        );
        
        if (!overlappingLeaves.isEmpty()) {
            throw new OverlappingLeaveException();
        }

        // Check available balance for ANNUAL leaves
        long workingDays = holidayService.calculateWorkingDays(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate());
        if (leaveRequestDTO.getLeaveType() == com.avi.leavemgmt.model.LeaveType.ANNUAL) {
            if (employee.getAnnualLeaveBalance() == null || employee.getAnnualLeaveBalance() < workingDays) {
                throw new InsufficientLeaveBalanceException((int) workingDays, 
                    employee.getAnnualLeaveBalance() != null ? employee.getAnnualLeaveBalance() : 0);
            }
        }
        
        LeaveRequest leaveRequest = convertToEntity(leaveRequestDTO);
        leaveRequest.setAppliedDate(LocalDate.now());
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);
        
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        
        // Log audit trail
        auditService.logLeaveAction(savedRequest.getId(), LeaveAudit.AuditAction.SUBMITTED,
                employee.getId(), employee.getName(), null, LeaveRequest.LeaveStatus.PENDING,
                null, "Leave request submitted for " + workingDays + " working days");
        
        return convertToDTO(savedRequest);
    }
    
    public LeaveRequestDTO approveLeaveRequest(Long id, Long managerId, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException(id));
        
        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException(leaveRequest.getStatus().name(), "PENDING");
        }
        
        // Validate manager has authority to approve this request
        Employee employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(leaveRequest.getEmployeeId()));
        
        if (!managerId.equals(employee.getManagerId())) {
            throw new UnauthorizedApprovalException(managerId, leaveRequest.getEmployeeId());
        }
        
        LeaveRequest.LeaveStatus oldStatus = leaveRequest.getStatus();
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.APPROVED);
        leaveRequest.setApprovedBy(managerId);
        leaveRequest.setApprovedDate(LocalDate.now());
        leaveRequest.setComments(comments);

        // Deduct balance for ANNUAL leave upon approval
        if (leaveRequest.getLeaveType() == com.avi.leavemgmt.model.LeaveType.ANNUAL) {
            long workingDays = holidayService.calculateWorkingDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
            Employee emp = employeeRepository.findById(leaveRequest.getEmployeeId())
                    .orElseThrow(() -> new EmployeeNotFoundException(leaveRequest.getEmployeeId()));
            int remaining = Math.max(0, emp.getAnnualLeaveBalance() - (int) workingDays);
            emp.setAnnualLeaveBalance(remaining);
            employeeRepository.save(emp);
        }
        
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        
        // Log audit trail
        Employee manager = employeeRepository.findById(managerId).orElse(null);
        String managerName = manager != null ? manager.getName() : "Unknown";
        auditService.logLeaveAction(updatedRequest.getId(), LeaveAudit.AuditAction.APPROVED,
                managerId, managerName, oldStatus, LeaveRequest.LeaveStatus.APPROVED,
                comments, "Leave request approved by " + managerName);
        
        return convertToDTO(updatedRequest);
    }
    
    public LeaveRequestDTO rejectLeaveRequest(Long id, Long managerId, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException(id));
        
        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException(leaveRequest.getStatus().name(), "PENDING");
        }
        
        // Validate manager has authority to reject this request
        Employee employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new EmployeeNotFoundException(leaveRequest.getEmployeeId()));
        
        if (!managerId.equals(employee.getManagerId())) {
            throw new UnauthorizedApprovalException(managerId, leaveRequest.getEmployeeId());
        }
        
        LeaveRequest.LeaveStatus oldStatus = leaveRequest.getStatus();
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.REJECTED);
        leaveRequest.setApprovedBy(managerId);
        leaveRequest.setApprovedDate(LocalDate.now());
        leaveRequest.setComments(comments);
        
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        
        // Log audit trail
        Employee manager = employeeRepository.findById(managerId).orElse(null);
        String managerName = manager != null ? manager.getName() : "Unknown";
        auditService.logLeaveAction(updatedRequest.getId(), LeaveAudit.AuditAction.REJECTED,
                managerId, managerName, oldStatus, LeaveRequest.LeaveStatus.REJECTED,
                comments, "Leave request rejected by " + managerName);
        
        return convertToDTO(updatedRequest);
    }
    
    /**
     * Cancel a leave request (only allowed for pending requests by the employee)
     */
    public LeaveRequestDTO cancelLeaveRequest(Long id, Long employeeId, String reason) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new LeaveRequestNotFoundException(id));
        
        // Only the employee who submitted the request can cancel it
        if (!leaveRequest.getEmployeeId().equals(employeeId)) {
            throw new UnauthorizedApprovalException(employeeId, leaveRequest.getEmployeeId());
        }
        
        // Only pending requests can be cancelled
        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new InvalidLeaveStatusException(leaveRequest.getStatus().name(), "PENDING");
        }
        
        LeaveRequest.LeaveStatus oldStatus = leaveRequest.getStatus();
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.CANCELLED);
        leaveRequest.setComments(reason);
        
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        
        // Log audit trail
        Employee employee = employeeRepository.findById(employeeId).orElse(null);
        String employeeName = employee != null ? employee.getName() : "Unknown";
        auditService.logLeaveAction(updatedRequest.getId(), LeaveAudit.AuditAction.CANCELLED,
                employeeId, employeeName, oldStatus, LeaveRequest.LeaveStatus.CANCELLED,
                reason, "Leave request cancelled by " + employeeName);
        
        return convertToDTO(updatedRequest);
    }
    
    public List<LeaveRequestDTO> getLeaveRequestsForManager(Long managerId) {
        return leaveRequestRepository.findByTeamMembersForManager(managerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private LeaveRequestDTO convertToDTO(LeaveRequest leaveRequest) {
        LeaveRequestDTO dto = new LeaveRequestDTO();
        dto.setId(leaveRequest.getId());
        dto.setEmployeeId(leaveRequest.getEmployeeId());
        dto.setLeaveType(leaveRequest.getLeaveType());
        dto.setStartDate(leaveRequest.getStartDate());
        dto.setEndDate(leaveRequest.getEndDate());
        dto.setReason(leaveRequest.getReason());
        dto.setStatus(leaveRequest.getStatus());
        dto.setAppliedDate(leaveRequest.getAppliedDate());
        dto.setApprovedBy(leaveRequest.getApprovedBy());
        dto.setApprovedDate(leaveRequest.getApprovedDate());
        dto.setComments(leaveRequest.getComments());
        
        // Calculate working days using holiday service
        dto.setWorkingDays(holidayService.calculateWorkingDays(leaveRequest.getStartDate(), leaveRequest.getEndDate()));
        
        // Set employee name
        employeeRepository.findById(leaveRequest.getEmployeeId())
                .ifPresent(employee -> dto.setEmployeeName(employee.getName()));
        
        // Set approver name
        if (leaveRequest.getApprovedBy() != null) {
            employeeRepository.findById(leaveRequest.getApprovedBy())
                    .ifPresent(approver -> dto.setApprovedByName(approver.getName()));
        }
        
        return dto;
    }
    
    private LeaveRequest convertToEntity(LeaveRequestDTO dto) {
        LeaveRequest leaveRequest = new LeaveRequest();
        leaveRequest.setId(dto.getId());
        leaveRequest.setEmployeeId(dto.getEmployeeId());
        leaveRequest.setLeaveType(dto.getLeaveType());
        leaveRequest.setStartDate(dto.getStartDate());
        leaveRequest.setEndDate(dto.getEndDate());
        leaveRequest.setReason(dto.getReason());
        if (dto.getStatus() != null) {
            leaveRequest.setStatus(dto.getStatus());
        }
        return leaveRequest;
    }
}