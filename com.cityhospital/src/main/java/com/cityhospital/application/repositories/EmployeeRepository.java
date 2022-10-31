package com.cityhospital.application.repositories;

import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    //Create a route to get all doctors.
    @Query(value = "SELECT * FROM employees e ORDER BY e.name", nativeQuery = true)
    List<Employee> getAllDoctors();

    // Create a route to get a doctor by employee_id.
    @Query(value = "SELECT * FROM employees WHERE employee_id = ?1", nativeQuery = true)
    Optional<Employee> findDoctorById(Long id);

    // Create a route to get doctors by status.
    @Query(value = "SELECT * FROM employees WHERE status LIKE %?1", nativeQuery = true)
    List<Employee> findByDoctorStatus(Status status);
}