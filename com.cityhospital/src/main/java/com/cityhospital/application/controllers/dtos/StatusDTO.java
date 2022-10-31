package com.cityhospital.application.controllers.dtos;

import com.cityhospital.application.enums.Status;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

public class StatusDTO {

    private Status status;

    public StatusDTO(Status status) {
        setStatus(status);
    }

    public StatusDTO() {
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
}
