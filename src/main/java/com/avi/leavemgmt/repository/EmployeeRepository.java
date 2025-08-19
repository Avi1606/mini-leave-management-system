package com.avi.leavemgmt.repository;

import com.avi.leavemgmt.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    
    Optional<Employee> findByEmail(String email);
    
    List<Employee> findByDepartment(String department);
    
    List<Employee> findByRole(String role);
    
    List<Employee> findByManagerId(Long managerId);
    
    @Query("SELECT e FROM Employee e WHERE e.managerId = :managerId")
    List<Employee> findTeamMembers(@Param("managerId") Long managerId);
    
    boolean existsByEmail(String email);
}