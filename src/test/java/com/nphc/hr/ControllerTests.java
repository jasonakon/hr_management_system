package com.nphc.hr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nphc.hr.controller.EmployeeController;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.exceptions.EmployeeCrudException;
import com.nphc.hr.repository.EmployeeRepo;
import com.nphc.hr.services.CsvFileService;
import com.nphc.hr.services.EmployeeService;
import org.apache.tomcat.util.http.fileupload.RequestContext;
import org.assertj.core.api.Assertions;
import org.junit.Assert;
import org.junit.Before;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SalaryManagementApplication.class)
@WebAppConfiguration
class ControllerTests {

    @InjectMocks
    EmployeeController employeeController;

    @InjectMocks
    EmployeeService employeeService;

    @Mock
    EmployeeRepo employeeRepo;

    @Before
    public void init(){
        MockitoAnnotations.;
    }

    @Test
    void testCreateUser() throws JsonProcessingException, EmployeeCrudException {
        MockHttpServletRequest request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));

        EmployeeDto employee = new EmployeeDto();
        employee.setId("DE0001");
        employee.setLogin("hello_world");
        employee.setStartDate("2021/04/22");
        employee.setName("Helo name");
        employee.setSalary(3360.33);

        when(employeeRepo.save(any(EmployeeDto.class))).thenReturn(employee);

        String employeeJson = new ObjectMapper().writer().writeValueAsString(employee);

        ResponseEntity<Object> responseEntity = employeeController.createUser(employeeJson);

        EmployeeDto employe2e = new EmployeeDto();

        Assertions.assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
    }
}
