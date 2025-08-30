package com.avi.leavemgmt.repository;

import com.avi.leavemgmt.model.LeaveAudit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LeaveAuditRepository extends JpaRepository<LeaveAudit, Long> {
    
    List<LeaveAudit> findByLeaveRequestIdOrderByActionTimestampDesc(Long leaveRequestId);
    
    List<LeaveAudit> findByPerformedByOrderByActionTimestampDesc(Long performedBy);
    
    @Query("SELECT la FROM LeaveAudit la WHERE la.actionTimestamp >= :startTime AND la.actionTimestamp <= :endTime ORDER BY la.actionTimestamp DESC")
    List<LeaveAudit> findByActionTimestampBetween(@Param("startTime") LocalDateTime startTime, @Param("endTime") LocalDateTime endTime);
    
    @Query("SELECT la FROM LeaveAudit la WHERE la.action = :action ORDER BY la.actionTimestamp DESC")
    List<LeaveAudit> findByAction(@Param("action") LeaveAudit.AuditAction action);
}