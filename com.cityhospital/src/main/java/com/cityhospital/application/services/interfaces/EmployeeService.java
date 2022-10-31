package com.cityhospital.application.services.interfaces;
import com.cityhospital.application.controllers.dtos.DepartmentDTO;
import com.cityhospital.application.controllers.dtos.StatusDTO;
import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Employee;

import java.util.List;

public interface EmployeeService  {

    List<Employee> getAllDoctors();
    Employee findByEmployeeId(Long employeeId);
    Employee findByEmployeeIdPV(Long employeeId);
    List<Employee> findByEmployeeByStatus(Status status);
    Employee addNewEmployee(Employee employee);
    void updateEmployeeStatus(Long employeeId, StatusDTO statusDTO);
    void updateEmployeeDepartment(Long employeeId, DepartmentDTO departmentDTO);

}
