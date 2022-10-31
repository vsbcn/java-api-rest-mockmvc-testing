package com.cityhospital.application.controllers.impl;
import com.cityhospital.application.enums.*;
import com.cityhospital.application.models.Employee;
import com.cityhospital.application.models.Patient;
import com.cityhospital.application.repositories.EmployeeRepository;
import com.cityhospital.application.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDate;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class PatientControllerImplTest {

    @Autowired
    private WebApplicationContext webApplicationContext;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();

    @Autowired
    private PatientRepository patientRepository;
    @Autowired
    private EmployeeRepository employeeRepository;
    private Employee employee1;
    private Employee employee2;
    private Patient patient1;
    private Patient patient2;
    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

        employee1 = employeeRepository.save(new Employee(356712l, "cardiology", "Alonso Flores", Status.ON_CALL));
        employee2 = employeeRepository.save(new Employee(356735l, "mental_health", "Luis Sarto", Status.ON));

        patient1 = patientRepository.save(new Patient("Jaime Jordan", LocalDate.of(1984, 03, 02), employee1));
        patient2 = patientRepository.save(new Patient("Louis Nelleke", LocalDate.of(1979, 05, 07), employee2));
    }

    @AfterEach
    void tearDown() {
        patientRepository.deleteById(patient1.getPatientId());
        patientRepository.deleteById(patient2.getPatientId());

        employeeRepository.deleteById(employee1.getEmployeeId());
        employeeRepository.deleteById(employee2.getEmployeeId());
    }

    @Test
    void findPatientByPatientId_FindExistingPatientId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patient/10"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jaime Jordan"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"employee\":{\"employeeId\":356712,\"department\":\"cardiology\",\"name\":\"Alonso Flores\",\"status\":\"ON_CALL\"}"));
    }

    @Test
    void findPatientByPatientIdPV_NotExistingPatient_404() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patient/1"))
                .andExpect(status().isNotFound())
                .andReturn();
    }

    @Test
    void findPatientByPatientIdPV_NotExistingPatient_400() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patient/s"))
                .andExpect(status().isBadRequest())
                .andReturn();
    }

    @Test
    void findPatientsByBirth_FindExistingPatient() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients-by-dob?min=1931-01-01&max=1999-01-01"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Jaime Jordan"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"employee\":{\"employeeId\":356712,\"department\":\"cardiology\",\"name\":\"Alonso Flores\",\"status\":\"ON_CALL\"}"));
    }

    @Test
    void findPatientsByAdmittingDoctorDepartment_FindExistingPatient() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients-by-doctor-department?department=mental_health"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Louis Nelleke"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"employee\":{\"employeeId\":356735,\"department\":\"mental_health\",\"name\":\"Luis Sarto\",\"status\":\"ON\"}"));
    }

    @Test
    void findPatientsByAdmittingDoctorStatus_FindExistingPatient() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/patients-by-doctor-status?status=ON"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Louis Nelleke"));
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"employee\":{\"employeeId\":356735,\"department\":\"mental_health\",\"name\":\"Luis Sarto\",\"status\":\"ON\"}"));
    }

    @Test
    void addNewPatient_ValidPatient() throws Exception {
        Employee employee3 = employeeRepository.save(new Employee(35673113l, "mental_health", "Luis Sarto", Status.ON));
        Patient patient3 = patientRepository.save(new Patient("Juan Rodriguez", LocalDate.of(1979, 05, 07), employee3));

        String body = objectMapper.writeValueAsString(patient3);

        System.out.println(body);

        MvcResult mvcResult = mockMvc.perform(post("/patient-add-new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"name\":\"Juan Rodriguez\",\"dateOfBirth\":\"1979-05-07\",\"employee\":{\"employeeId\":35673113,\"department\":\"mental_health\",\"name\":\"Luis Sarto\",\"status\":\"ON\"}"));
    }

    @Test
    void updatePatient_UpdatePatient_PatientUpdated() throws Exception {
        Patient patient3 = new Patient("Luis Fernandez", LocalDate.of(1979, 05, 07),employee1);

        String body = objectMapper.writeValueAsString(patient3);
        MvcResult mvcResult = mockMvc.perform(put("/patient-update/" + patient2.getPatientId())
                .content(body)
                .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(patient2.toString());

        assertEquals(204, mvcResult.getResponse().getStatus());
        Optional<Patient> patientOptional = patientRepository.findById(patient2.getPatientId());
        assertEquals("Luis Fernandez", patientOptional.get().getName());
    }

    @Test
    void updatePatient_UpdatePatient_404() throws Exception {
        Patient patient3 = new Patient("Luis Fernandez", LocalDate.of(1979, 05, 07),employee1);

        String body = objectMapper.writeValueAsString(patient3);
        MvcResult mvcResult = mockMvc.perform(put("/patient-update/115")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        System.out.println(patient2.toString());

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    void updatePatient_UpdatePatient_400() throws Exception {
        Patient patient3 = new Patient("", LocalDate.of(1979, 05, 07),employee1);

        String body = objectMapper.writeValueAsString(patient3);
        MvcResult mvcResult = mockMvc.perform(put("/patient-update/" + patient2.getPatientId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

}