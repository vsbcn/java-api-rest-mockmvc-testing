package com.cityhospital.application.controllers.impl;
import com.cityhospital.application.controllers.interfaces.*;
import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Patient;
import com.cityhospital.application.repositories.PatientRepository;
import com.cityhospital.application.services.interfaces.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
public class PatientControllerImpl implements PatientController {
    @Autowired
    private PatientService patientService;

    @GetMapping("/patient")
    public Patient findPatientByPatientId(@RequestParam Integer id) {
        return patientService.findPatientByPatientId(id);
    }

    @GetMapping("/patient/{id}")
    public Patient findPatientByPatientIdPV(@PathVariable(name = "id") Integer id) {
        return patientService.findPatientByPatientIdPV(id);
    }

    @GetMapping("/patients-by-dob")
    public List<Patient> findPatientsByBirth(@RequestParam String min, @RequestParam String max) {
        return patientService.findPatientsByBirth(min, max);
    }

    @GetMapping("/patients-by-doctor-department")
    public List<Patient> findPatientsByAdmittingDoctorDepartment(@RequestParam String department) {
        return patientService.findPatientsByAdmittingDoctorDepartment(department);
    }

    @GetMapping("/patients-by-doctor-status")
    public List<Patient> findPatientsByAdmittingDoctorStatus(@RequestParam Status status) {
        return patientService.findPatientsByAdmittingDoctorStatus(status);
    }

    @PostMapping("/patient-add-new")
    @ResponseStatus(HttpStatus.CREATED)
    public Patient addNewPatient(@RequestBody @Valid Patient patient) {
        return patientService.addNewPatient(patient);
    }

    @PutMapping("/patient-update/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updatePatient(@PathVariable(name="id") Integer id, @RequestBody @Valid Patient patient) {
        patientService.updatePatient(id, patient);
    }
}
