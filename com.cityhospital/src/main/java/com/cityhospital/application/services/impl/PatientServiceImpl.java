package com.cityhospital.application.services.impl;
import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Patient;
import com.cityhospital.application.repositories.PatientRepository;
import com.cityhospital.application.services.interfaces.PatientService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PatientServiceImpl implements PatientService {

    @Autowired
    private PatientRepository patientRepository;

    public Patient findPatientByPatientId(Integer id) {
        Optional<Patient> patientByPatientId = patientRepository.findPatientByPatientId(id);
        if (patientByPatientId.isPresent()) {
            return patientByPatientId.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not exist");
        }
    }

    public Patient findPatientByPatientIdPV(Integer id) {
        Optional<Patient> patientByPatientId = patientRepository.findPatientByPatientId(id);
        if (patientByPatientId.isPresent()) {
            return patientByPatientId.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not exist");
        }
    }

    public List<Patient> findPatientsByBirth(String min, String max) {

        LocalDate minDate = LocalDate.parse(min);
        LocalDate maxDate = LocalDate.parse(max);

        return patientRepository.findPatientsByBirth(minDate, maxDate);
    }

    public List<Patient> findPatientsByAdmittingDoctorDepartment(String department) {
        List<Patient> patientsAdmitted = patientRepository.findPatientsByAdmittingDoctorDepartment(department);
        if (patientsAdmitted.iterator().hasNext()) {
            return patientsAdmitted;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found");
        }
    }

    public List<Patient> findPatientsByAdmittingDoctorStatus(Status status) {
        List<Patient> patientsAdmitted = patientRepository.findPatientsByAdmittingDoctorStatus(status);
        if (patientsAdmitted.iterator().hasNext()) {
            return patientsAdmitted;
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not found");
        }
    }

    public Patient addNewPatient(Patient patient) {
        patient.setPatientId(null);
        return patientRepository.save(patient);
    }

    public void updatePatient(Integer id, Patient patient) {
        if (patientRepository.existsById(id)) {
            patient.setPatientId(id);
            patientRepository.save(patient);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Patient not exist");
        }
    }

}
