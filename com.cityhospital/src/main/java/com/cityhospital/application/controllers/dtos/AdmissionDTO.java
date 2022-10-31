package com.cityhospital.application.controllers.dtos;

import com.cityhospital.application.models.Employee;

public class AdmissionDTO {
    private Employee employee;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }
}
