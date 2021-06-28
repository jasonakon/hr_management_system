package com.nphc.hr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nphc.hr.controller.EmployeeController;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.exceptions.EmployeeCrudException;
import com.nphc.hr.repository.EmployeeRepo;
import com.nphc.hr.services.CsvFileService;
import com.nphc.hr.services.EmployeeService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SalaryManagementApplication.class)
@WebAppConfiguration
class ControllerTests {

    @InjectMocks
    EmployeeController employeeController;

    @Mock
    EmployeeService employeeService;

    @Mock
    CsvFileService csvFileService;

    @Mock
    EmployeeRepo employeeRepo;

    @BeforeEach
    public void init() throws EmployeeCrudException, CsvValidationException {
        EmployeeDto employee = new EmployeeDto();
        employee.setId("DE0001");
        employee.setLogin("hello_world");
        employee.setStartDate("2021/04/22");
        employee.setName("Helo name");
        employee.setSalary(3360.33);
        String id = "DE0001";
        MultipartFile multipartFile = new MockMultipartFile("data", "filename.csv", "text/plain", "some csv".getBytes());

        doNothing().when(employeeService).createEmployee(employee);
        doNothing().when(employeeService).patchEmployee(employee,id);
        doNothing().when(csvFileService).processCsvFile(multipartFile);
        when(employeeRepo.save(any(EmployeeDto.class))).thenReturn(employee);
    }


    void testCreateUser(EmployeeDto employee) throws JsonProcessingException, EmployeeCrudException {
        String employeeJson = new ObjectMapper().writer().writeValueAsString(employee);
        ResponseEntity<Object> responseEntity = employeeController.createUser(employeeJson);
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    void testUpdateUser(EmployeeDto employee, String id) throws JsonProcessingException, EmployeeCrudException {
        String employeeJson = new ObjectMapper().writer().writeValueAsString(employee);
        ResponseEntity<Object> responseEntity = employeeController.updateUser(id,employeeJson);
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    void testDeleteUser(String id) throws EmployeeCrudException {
        ResponseEntity<Object> responseEntity = employeeController.deleteUser(id);
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    void testFileUpload() throws CsvValidationException {
        MultipartFile multipartFile = new MockMultipartFile("data", "filename.csv", "text/plain", "some csv".getBytes());
        ResponseEntity<Object> responseEntity = employeeController.fileUpload(multipartFile);
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }

    void testGetUser(String id) throws EmployeeCrudException {
        ResponseEntity<Object> responseEntity = employeeController.getUsers(30.8,38.2,1,5,id);
        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test
    void testCRUD() throws JsonProcessingException, EmployeeCrudException, CsvValidationException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        String id = "DE0001";
        EmployeeDto employee = new EmployeeDto();
        employee.setId("DE0001");
        employee.setLogin("hello_world");
        employee.setStartDate("2021/04/22");
        employee.setName("Helo name");
        employee.setSalary(3360.33);

        // Test each controllers:
        testCreateUser(employee);
        testUpdateUser(employee,id);
        testDeleteUser(id);
        testFileUpload();
        testGetUser(id);
    }




}
