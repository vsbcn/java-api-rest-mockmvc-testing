package com.cityhospital.application.controllers.impl;

import com.cityhospital.application.controllers.dtos.DepartmentDTO;
import com.cityhospital.application.controllers.dtos.StatusDTO;
import com.cityhospital.application.enums.Status;
import com.cityhospital.application.models.Employee;
import com.cityhospital.application.models.Patient;
import com.cityhospital.application.repositories.EmployeeRepository;
import com.cityhospital.application.repositories.PatientRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.WebApplicationContext;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class EmployeeControllerImplTest {
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
    void getAllDoctors_getListOfAllDoctors() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/doctors"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());

        assertTrue(mvcResult.getResponse().getContentAsString().contains("Luis Sarto"));
    }

    @Test
    void findByEmployeeId_getEmployeeById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/doctor?employeeId=356735"))
                .andExpect(status().isOk())
                .andReturn();
        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Luis Sarto"));
    }

    @Test
    void findByEmployeeIdPV_getEmployeeByIdPV() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/doctor/356712"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Alonso Flores"));
    }

    @Test
    void findByEmployeeByStatus_getEmployeeByStatus() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/doctor/status?status=ON"))
                .andExpect(status().isOk())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("Luis Sarto"));
    }

    @Test
    void addNewPatient_ValidPatient() throws Exception {
        Employee employee3 = employeeRepository.save(new Employee(35673113l, "mental_health", "Luis Sarto", Status.ON));
        Patient patient3 = patientRepository.save(new Patient("Juan Rodriguez", LocalDate.of(1979, 05, 07), employee3));

        String body = objectMapper.writeValueAsString(employee3);

        System.out.println(body);

        MvcResult mvcResult = mockMvc.perform(post("/doctor/new")
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andReturn();

        System.out.println(mvcResult.getResponse().getContentAsString());
        assertTrue(mvcResult.getResponse().getContentAsString().contains("\"employeeId\":35673113,\"department\":\"mental_health\",\"name\":\"Luis Sarto\",\"status\":\"ON\""));
    }

    //patch DTO

//    @PatchMapping("/doctor-status/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void updateEmployeeStatus(@PathVariable(name="id") Long employeeId, @RequestBody @Valid StatusDTO statusDTO) {
//        employeeService.updateEmployeeStatus(employeeId, statusDTO);
//    }
//
//    @PatchMapping("/doctor-department/{id}")
//    @ResponseStatus(HttpStatus.NO_CONTENT)
//    public void updateEmployeeDepartment(@PathVariable(name="id")  Long employeeId, @RequestBody @Valid DepartmentDTO departmentDTO) {
//        employeeService.updateEmployeeDepartment(employeeId, departmentDTO);
//    }

    @Test
    void updateEmployeeStatus_updateEmployeeStatusOK() throws Exception {
        StatusDTO statusDTO = new StatusDTO(Status.OFF);
        String body = objectMapper.writeValueAsString(statusDTO);

        MvcResult mvcResult = mockMvc.perform(patch("/doctor-status/" + employee1.getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());

        Optional<Employee> optionalEmployee = employeeRepository.findById(employee1.getEmployeeId());
        assertEquals(Status.OFF, optionalEmployee.get().getStatus());
    }

    @Test
    void updateEmployeeStatus_updateEmployeeStatus_406() throws Exception {
        StatusDTO statusDTO = new StatusDTO();
        String body = objectMapper.writeValueAsString(statusDTO);

        MvcResult mvcResult = mockMvc.perform(patch("/doctor-status/" + employee1.getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(406, mvcResult.getResponse().getStatus());
    }

    @Test
    void updateEmployeeStatus_updateEmployeeStatus_404() throws Exception {
        StatusDTO statusDTO = new StatusDTO();
        String body = objectMapper.writeValueAsString(statusDTO);

        MvcResult mvcResult = mockMvc.perform(patch("/doctor-stats/" + employee1.getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }

    @Test
    void updateEmployeeDepartment_updateEmployeeDepartmentOK() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO("medical_assistance");
        String body = objectMapper.writeValueAsString(departmentDTO);

        MvcResult mvcResult = mockMvc.perform(patch("/doctor-department/" + employee1.getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(204, mvcResult.getResponse().getStatus());

        Optional<Employee> optionalEmployee = employeeRepository.findById(employee1.getEmployeeId());
        assertEquals("medical_assistance", optionalEmployee.get().getDepartment());
    }

    @Test
    void updateEmployeeDepartment_updateEmployeeDepartment_400() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO("");
        String body = objectMapper.writeValueAsString(departmentDTO);

        MvcResult mvcResult = mockMvc.perform(patch("/doctor-department/" + employee1.getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(400, mvcResult.getResponse().getStatus());
    }

    @Test
    void updateEmployeeDepartment_updateEmployeeDepartment_404() throws Exception {
        DepartmentDTO departmentDTO = new DepartmentDTO("medical_assistance");
        String body = objectMapper.writeValueAsString(departmentDTO);

        MvcResult mvcResult = mockMvc.perform(patch("/doctor-departmen/" + employee1.getEmployeeId())
                        .content(body)
                        .contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        assertEquals(404, mvcResult.getResponse().getStatus());
    }



}