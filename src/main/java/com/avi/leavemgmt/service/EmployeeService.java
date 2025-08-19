package com.avi.leavemgmt.service;

import com.avi.leavemgmt.dto.EmployeeDTO;
import com.avi.leavemgmt.model.Employee;
import com.avi.leavemgmt.repository.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EmployeeService {
    
    private final EmployeeRepository employeeRepository;
    
    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }
    
    public List<EmployeeDTO> getAllEmployees() {
        return employeeRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public Optional<EmployeeDTO> getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .map(this::convertToDTO);
    }
    
    public EmployeeDTO createEmployee(EmployeeDTO employeeDTO) {
        if (employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        
        Employee employee = convertToEntity(employeeDTO);
        Employee savedEmployee = employeeRepository.save(employee);
        return convertToDTO(savedEmployee);
    }
    
    public EmployeeDTO updateEmployee(Long id, EmployeeDTO employeeDTO) {
        Employee existingEmployee = employeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Employee not found with id: " + id));
        
        // Check if email is being changed and if new email already exists
        if (!existingEmployee.getEmail().equals(employeeDTO.getEmail()) &&
            employeeRepository.existsByEmail(employeeDTO.getEmail())) {
            throw new RuntimeException("Employee with email " + employeeDTO.getEmail() + " already exists");
        }
        
        existingEmployee.setName(employeeDTO.getName());
        existingEmployee.setEmail(employeeDTO.getEmail());
        existingEmployee.setDepartment(employeeDTO.getDepartment());
        existingEmployee.setRole(employeeDTO.getRole());
        existingEmployee.setManagerId(employeeDTO.getManagerId());
        
        Employee updatedEmployee = employeeRepository.save(existingEmployee);
        return convertToDTO(updatedEmployee);
    }
    
    public void deleteEmployee(Long id) {
        if (!employeeRepository.existsById(id)) {
            throw new RuntimeException("Employee not found with id: " + id);
        }
        employeeRepository.deleteById(id);
    }
    
    public List<EmployeeDTO> getEmployeesByDepartment(String department) {
        return employeeRepository.findByDepartment(department).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    public List<EmployeeDTO> getTeamMembers(Long managerId) {
        return employeeRepository.findByManagerId(managerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    private EmployeeDTO convertToDTO(Employee employee) {
        EmployeeDTO dto = new EmployeeDTO();
        dto.setId(employee.getId());
        dto.setName(employee.getName());
        dto.setEmail(employee.getEmail());
        dto.setDepartment(employee.getDepartment());
        dto.setRole(employee.getRole());
        dto.setManagerId(employee.getManagerId());
        
        // Set manager name if available
        if (employee.getManagerId() != null) {
            employeeRepository.findById(employee.getManagerId())
                    .ifPresent(manager -> dto.setManagerName(manager.getName()));
        }
        
        return dto;
    }
    
    private Employee convertToEntity(EmployeeDTO dto) {
        Employee employee = new Employee();
        employee.setId(dto.getId());
        employee.setName(dto.getName());
        employee.setEmail(dto.getEmail());
        employee.setDepartment(dto.getDepartment());
        employee.setRole(dto.getRole());
        employee.setManagerId(dto.getManagerId());
        return employee;
    }
}