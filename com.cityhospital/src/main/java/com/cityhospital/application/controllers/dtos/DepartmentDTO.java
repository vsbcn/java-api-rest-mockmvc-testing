package com.cityhospital.application.controllers.dtos;

import javax.validation.constraints.NotEmpty;

public class DepartmentDTO {
    @NotEmpty(message = "The name should be not empty.")
    private String department;

    public DepartmentDTO(String department) {
        this.department = department;
    }

    public DepartmentDTO() {
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
