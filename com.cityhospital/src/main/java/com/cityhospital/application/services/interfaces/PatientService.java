package com.cityhospital.application.services.interfaces;
import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Patient;

import java.util.List;

public interface PatientService {
    Patient findPatientByPatientId(Integer id);
    Patient findPatientByPatientIdPV(Integer id);
    List<Patient> findPatientsByBirth(String min, String max);
    List<Patient> findPatientsByAdmittingDoctorDepartment(String department);
    List<Patient> findPatientsByAdmittingDoctorStatus(Status status);
    Patient addNewPatient(Patient patient);
    void updatePatient(Integer id, Patient patient);
}
