package com.cityhospital.application.controllers.repositories;

import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Employee;
import com.cityhospital.application.models.Patient;
import com.cityhospital.application.repositories.EmployeeRepository;
import com.cityhospital.application.repositories.PatientRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;


@SpringBootTest
class PatientRepositoryTest {

    @Autowired
    private PatientRepository patientRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void setUp() {
//        List<Employee> employeeList = new ArrayList<>();
//        employeeList.add(new Employee(356712l, "cardiology", "Alonso Flores", Status.ON_CALL));
//        employeeList.add(new Employee(564134l, "immunology", "Sam Ortega", Status.ON));
//        employeeList.add(new Employee(761527l, "cardiology", "German Ruiz", Status.OFF));
//        employeeList.add(new Employee(166552l, "pulmonary", "Maria Lin", Status.ON));
//        employeeList.add(new Employee(156545l, "orthopaedic", "Paolo Rodriguez", Status.ON_CALL));
//        employeeList.add(new Employee(172456l, "psychiatric", "John Paul Armes", Status.OFF));
//        employeeRepository.saveAll(employeeList);
//
//        List<Patient> patientList = new ArrayList<>();
//        patientList.add(new Patient("Jaime Jordan", LocalDate.of(1984, 03, 02), employeeList.get(1)));
//        patientList.add(new Patient("Marian Garcia", LocalDate.of(1972, 01, 12), employeeList.get(1)));
//        patientList.add(new Patient("Julia Dusterdieck", LocalDate.of(1954, 06, 11), employeeList.get(0)));
//        patientList.add(new Patient("Steve McDuck", LocalDate.of(1931, 11, 10), employeeList.get(2)));
//        patientList.add(new Patient("Marian Garcia", LocalDate.of(1999, 02, 15), employeeList.get(5)));
//        patientRepository.saveAll(patientList);
    }

    @AfterEach
    void tearDown() {
//        patientRepository.deleteAll();
//        employeeRepository.deleteAll();
    }

    @Test
    public void getAllDoctors() {
        List<Employee> employeeList = employeeRepository.getAllDoctors();

        Assertions.assertEquals(6, employeeList.size());
        Assertions.assertEquals("Alonso Flores", employeeList.get(0).getName());
    }

    @Test
    public void findByEmployeeById() {
        Optional<Employee> employeeOptional = employeeRepository.findDoctorById(156545l);

        Assertions.assertTrue(employeeOptional.isPresent());
        Assertions.assertEquals("Paolo Rodriguez",employeeOptional.get().getName());
    }

    @Test
    public void findByEmployeeByStatus() {
        List<Employee> employeeList = employeeRepository.findByDoctorStatus(Status.ON_CALL);

        Assertions.assertEquals(2, employeeList.size());
        Assertions.assertEquals(Status.ON_CALL, employeeList.get(0).getStatus());
    }

    @Test
    public void findPatientByPatientId() {
        Optional<Patient> patientOptional = patientRepository.findPatientByPatientId(72);

        Assertions.assertTrue(patientOptional.isPresent());
        Assertions.assertEquals("Jaime Jordan", patientOptional.get().getName());
    }

    @Test
    public void findPatientByBirth() {

        String min, max;
        min = "1931-01-01";
        max = "1999-01-01";

        LocalDate minDate = LocalDate.parse(min);
        LocalDate maxDate = LocalDate.parse(max);

        List<Patient> patientsByBirth = patientRepository.findPatientsByBirth(minDate, maxDate);

        Assertions.assertTrue(!patientsByBirth.isEmpty());
        Assertions.assertEquals("Jaime Jordan", patientsByBirth.get(0).getName());
    }

    @Test
    public void findPatientsByAdmittingDoctorDepartment() {
        List<Patient> patientsByAdmittingDoctorDepartment = patientRepository.findPatientsByAdmittingDoctorDepartment("cardiology");

        Assertions.assertEquals(2,patientsByAdmittingDoctorDepartment.size());
        Assertions.assertEquals("Julia Dusterdieck", patientsByAdmittingDoctorDepartment.get(0).getName());
    }

    @Test
    public void findPatientsByAdmittingDoctorStatus() {
        List<Patient> patientsByAdmittingDoctorId = patientRepository.findPatientsByAdmittingDoctorStatus(Status.OFF);

        Assertions.assertEquals(2,patientsByAdmittingDoctorId.size());
        Assertions.assertEquals("Marian Garcia", patientsByAdmittingDoctorId.get(0).getName());
    }
}