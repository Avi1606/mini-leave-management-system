package com.avi.leavemgmt.service;

import com.avi.leavemgmt.dto.LeaveRequestDTO;
import com.avi.leavemgmt.model.Employee;
import com.avi.leavemgmt.model.LeaveRequest;
import com.avi.leavemgmt.repository.EmployeeRepository;
import com.avi.leavemgmt.repository.LeaveRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class LeaveRequestService {
    
    private final LeaveRequestRepository leaveRequestRepository;
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public LeaveRequestService(LeaveRequestRepository leaveRequestRepository, 
                              EmployeeRepository employeeRepository) {
        this.leaveRequestRepository = leaveRequestRepository;
        this.employeeRepository = employeeRepository;
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
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + leaveRequestDTO.getEmployeeId()));
        
        // Validate dates
        if (leaveRequestDTO.getStartDate().isAfter(leaveRequestDTO.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }
        
        if (leaveRequestDTO.getStartDate().isBefore(LocalDate.now())) {
            throw new RuntimeException("Cannot apply for leave in the past");
        }

        // Validate start date must not be before joining date
        if (leaveRequestDTO.getStartDate().isBefore(employee.getJoiningDate())) {
            throw new RuntimeException("Cannot apply for leave before joining date");
        }
        
        // Check for overlapping approved leaves
        List<LeaveRequest> overlappingLeaves = leaveRequestRepository.findOverlappingApprovedLeaves(
                leaveRequestDTO.getEmployeeId(),
                leaveRequestDTO.getStartDate(),
                leaveRequestDTO.getEndDate()
        );
        
        if (!overlappingLeaves.isEmpty()) {
            throw new RuntimeException("Leave request overlaps with existing approved leave");
        }

        // Check available balance for ANNUAL leaves
        long workingDays = calculateWorkingDays(leaveRequestDTO.getStartDate(), leaveRequestDTO.getEndDate());
        if (leaveRequestDTO.getLeaveType() == com.avi.leavemgmt.model.LeaveType.ANNUAL) {
            if (employee.getAnnualLeaveBalance() == null || employee.getAnnualLeaveBalance() < workingDays) {
                throw new RuntimeException("Requested leave days exceed available annual leave balance");
            }
        }
        
        LeaveRequest leaveRequest = convertToEntity(leaveRequestDTO);
        leaveRequest.setAppliedDate(LocalDate.now());
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.PENDING);
        
        LeaveRequest savedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(savedRequest);
    }
    
    public LeaveRequestDTO approveLeaveRequest(Long id, Long managerId, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
        
        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Leave request is not in pending status");
        }
        
        // Validate manager has authority to approve this request
        Employee employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (!managerId.equals(employee.getManagerId())) {
            throw new RuntimeException("Manager does not have authority to approve this leave request");
        }
        
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.APPROVED);
        leaveRequest.setApprovedBy(managerId);
        leaveRequest.setApprovedDate(LocalDate.now());
        leaveRequest.setComments(comments);

        // Deduct balance for ANNUAL leave upon approval
        if (leaveRequest.getLeaveType() == com.avi.leavemgmt.model.LeaveType.ANNUAL) {
            long workingDays = calculateWorkingDays(leaveRequest.getStartDate(), leaveRequest.getEndDate());
            Employee emp = employeeRepository.findById(leaveRequest.getEmployeeId())
                    .orElseThrow(() -> new RuntimeException("Employee not found"));
            int remaining = Math.max(0, emp.getAnnualLeaveBalance() - (int) workingDays);
            emp.setAnnualLeaveBalance(remaining);
            employeeRepository.save(emp);
        }
        
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(updatedRequest);
    }
    
    public LeaveRequestDTO rejectLeaveRequest(Long id, Long managerId, String comments) {
        LeaveRequest leaveRequest = leaveRequestRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Leave request not found with id: " + id));
        
        if (leaveRequest.getStatus() != LeaveRequest.LeaveStatus.PENDING) {
            throw new RuntimeException("Leave request is not in pending status");
        }
        
        // Validate manager has authority to reject this request
        Employee employee = employeeRepository.findById(leaveRequest.getEmployeeId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        
        if (!managerId.equals(employee.getManagerId())) {
            throw new RuntimeException("Manager does not have authority to reject this leave request");
        }
        
        leaveRequest.setStatus(LeaveRequest.LeaveStatus.REJECTED);
        leaveRequest.setApprovedBy(managerId);
        leaveRequest.setApprovedDate(LocalDate.now());
        leaveRequest.setComments(comments);
        
        LeaveRequest updatedRequest = leaveRequestRepository.save(leaveRequest);
        return convertToDTO(updatedRequest);
    }
    
    public List<LeaveRequestDTO> getLeaveRequestsForManager(Long managerId) {
        return leaveRequestRepository.findByTeamMembersForManager(managerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private Long calculateWorkingDays(LocalDate startDate, LocalDate endDate) {
        long workingDays = 0;
        LocalDate currentDate = startDate;
        
        while (!currentDate.isAfter(endDate)) {
            DayOfWeek dayOfWeek = currentDate.getDayOfWeek();
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                workingDays++;
            }
            currentDate = currentDate.plusDays(1);
        }
        
        return workingDays;
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
        
        // Calculate working days
        dto.setWorkingDays(calculateWorkingDays(leaveRequest.getStartDate(), leaveRequest.getEndDate()));
        
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