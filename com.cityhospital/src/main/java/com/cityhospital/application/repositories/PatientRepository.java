package com.cityhospital.application.repositories;

import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Patient;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface PatientRepository extends JpaRepository<Patient, Integer> {

    // Create a route to get a patient by patient_id.
    @Query(value = "SELECT * FROM patients p WHERE patient_id = ?1", nativeQuery = true)
    Optional<Patient> findPatientByPatientId(Integer id);

    // Create a route to get patients date of birth within a specified range.
    @Query(value = "SELECT * FROM patients p WHERE date_of_birth BETWEEN ?1 AND ?2 ORDER BY p.name", nativeQuery = true)
    List<Patient> findPatientsByBirth(LocalDate dateMin, LocalDate dateMax);

    // Create a route to get patients by the department that their admitting doctor is in.
    // (For example, get all patients admitted by a doctor in cardiology).
    @Query(value = "SELECT * FROM patients p INNER JOIN employees e ON p.admitted_by = e.employee_id WHERE e.department = ?1 ORDER BY p.name", nativeQuery = true)
    List<Patient> findPatientsByAdmittingDoctorDepartment(String department);

    @Query(value = "SELECT * FROM patients p INNER JOIN employees e ON p.admitted_by = e.employee_id WHERE e.status LIKE %?1% ORDER BY p.name", nativeQuery = true)
    // Create a route to get all patients with a doctor whose status is OFF.
    List<Patient> findPatientsByAdmittingDoctorStatus(Status status);
}