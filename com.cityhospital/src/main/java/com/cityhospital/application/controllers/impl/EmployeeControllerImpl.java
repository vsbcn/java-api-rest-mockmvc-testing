package com.cityhospital.application.controllers.impl;

import com.cityhospital.application.controllers.dtos.DepartmentDTO;
import com.cityhospital.application.controllers.dtos.StatusDTO;
import com.cityhospital.application.controllers.interfaces.*;
import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Employee;
import com.cityhospital.application.services.interfaces.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class EmployeeControllerImpl implements EmployeeController {
    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/doctors")
    public List<Employee> getAllDoctors() {
        return employeeService.getAllDoctors();
    }

    @GetMapping("/doctor")
    public Employee findByEmployeeId(@RequestParam Long employeeId) {
        return employeeService.findByEmployeeId(employeeId);
    }

    @GetMapping("/doctor/{id}")
    public Employee findByEmployeeIdPV(@PathVariable(name = "id") Long employeeId) {
        return employeeService.findByEmployeeIdPV(employeeId);
    }

    @GetMapping("/doctor/status")
    public List<Employee> findByEmployeeByStatus(@RequestParam Status status) {
        return employeeService.findByEmployeeByStatus(status);
    }

    @PostMapping("/doctor/new")
    @ResponseStatus(HttpStatus.CREATED)
    public Employee addNewEmployee(@RequestBody @Valid Employee employee) {
        return employeeService.addNewEmployee(employee);
    }

    @PatchMapping("/doctor-status/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployeeStatus(@PathVariable(name="id") Long employeeId, @RequestBody @Valid StatusDTO statusDTO) {
        employeeService.updateEmployeeStatus(employeeId, statusDTO);
    }

    @PatchMapping("/doctor-department/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateEmployeeDepartment(@PathVariable(name="id")  Long employeeId, @RequestBody @Valid DepartmentDTO departmentDTO) {
        employeeService.updateEmployeeDepartment(employeeId, departmentDTO);
    }

}
