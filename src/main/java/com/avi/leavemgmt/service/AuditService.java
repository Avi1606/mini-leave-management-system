package com.avi.leavemgmt.service;

import com.avi.leavemgmt.model.LeaveAudit;
import com.avi.leavemgmt.model.LeaveRequest;
import com.avi.leavemgmt.repository.LeaveAuditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Service for managing leave audit trails
 */
@Service
@Transactional
public class AuditService {
    
    private final LeaveAuditRepository leaveAuditRepository;
    
    @Autowired
    public AuditService(LeaveAuditRepository leaveAuditRepository) {
        this.leaveAuditRepository = leaveAuditRepository;
    }
    
    /**
     * Log a leave request action
     */
    public void logLeaveAction(Long leaveRequestId, LeaveAudit.AuditAction action, Long performedBy, 
                              String performedByName, LeaveRequest.LeaveStatus oldStatus, 
                              LeaveRequest.LeaveStatus newStatus, String comments, String details) {
        LeaveAudit audit = new LeaveAudit(leaveRequestId, action, performedBy, performedByName);
        audit.setOldStatus(oldStatus);
        audit.setNewStatus(newStatus);
        audit.setComments(comments);
        audit.setDetails(details);
        
        leaveAuditRepository.save(audit);
    }
    
    /**
     * Get audit trail for a specific leave request
     */
    public List<LeaveAudit> getLeaveRequestAuditTrail(Long leaveRequestId) {
        return leaveAuditRepository.findByLeaveRequestIdOrderByActionTimestampDesc(leaveRequestId);
    }
    
    /**
     * Get audit entries for a specific user
     */
    public List<LeaveAudit> getUserAuditHistory(Long userId) {
        return leaveAuditRepository.findByPerformedByOrderByActionTimestampDesc(userId);
    }
    
    /**
     * Get audit entries by action type
     */
    public List<LeaveAudit> getAuditByAction(LeaveAudit.AuditAction action) {
        return leaveAuditRepository.findByAction(action);
    }
    
    /**
     * Get audit entries in date range
     */
    public List<LeaveAudit> getAuditInDateRange(LocalDateTime startTime, LocalDateTime endTime) {
        return leaveAuditRepository.findByActionTimestampBetween(startTime, endTime);
    }
}