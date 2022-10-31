package com.cityhospital.application.services.impl;

import com.cityhospital.application.controllers.dtos.DepartmentDTO;
import com.cityhospital.application.controllers.dtos.StatusDTO;
import com.cityhospital.application.enums.Status;
import com.cityhospital.application.models.Employee;
import com.cityhospital.application.repositories.EmployeeRepository;
import com.cityhospital.application.services.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeServiceImpl implements EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public List<Employee> getAllDoctors() {
        return employeeRepository.getAllDoctors();
    }

    public Employee findByEmployeeId(Long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findDoctorById(employeeId);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found.");
        }
    }

    public Employee findByEmployeeIdPV(Long employeeId) {
        Optional<Employee> employeeOptional = employeeRepository.findDoctorById(employeeId);
        if (employeeOptional.isPresent()) {
            return employeeOptional.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Doctor not found.");
        }
    }

    public List<Employee> findByEmployeeByStatus(Status status) {
        return employeeRepository.findByDoctorStatus(status);
    }

    public Employee addNewEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    public void updateEmployeeStatus(Long employeeId, StatusDTO statusDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findDoctorById(employeeId);
        if (employeeOptional.isPresent()) {
            if (statusDTO.getStatus() != null) {
                employeeOptional.get().setStatus(statusDTO.getStatus());
                employeeRepository.save(employeeOptional.get());
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_ACCEPTABLE, "The status should not be null");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

    public void updateEmployeeDepartment(Long employeeId, DepartmentDTO departmentDTO) {
        Optional<Employee> employeeOptional = employeeRepository.findDoctorById(employeeId);
        if (employeeOptional.isPresent()) {
            employeeOptional.get().setDepartment(departmentDTO.getDepartment());
            employeeRepository.save(employeeOptional.get());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }
    }

}
