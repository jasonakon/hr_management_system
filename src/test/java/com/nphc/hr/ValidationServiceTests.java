package com.nphc.hr;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.dto.EmployeeValidDto;
import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.repository.EmployeeRepo;
import com.nphc.hr.services.CsvFileService;
import com.nphc.hr.services.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SalaryManagementApplication.class)
@WebAppConfiguration
class ValidationServiceTests {
    @InjectMocks
    ValidationService validationService;

    @InjectMocks
    CsvFileService csvFileService;

    @Mock
    EmployeeRepo employeeRepo;

    @Test
    void testCsvIsValid() throws IOException, CsvValidationException {
        String resourceName = "csv/employee_test_5.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        byte[] content = Files.readAllBytes(file.getAbsoluteFile().toPath());
        MultipartFile multipartFile = new MockMultipartFile("data", "filename.csv", "text/plain", content);
        List<String[]> csvList = csvFileService.readCsvFile(multipartFile);

        CsvValidDto csvValid = new CsvValidDto(true);
        EmployeeValidDto employeeValidDto = new EmployeeValidDto(true);

        List<String> loginList = Arrays.asList("Jason", "WongJun", "PPHeng");
        List<String> idList = Arrays.asList("EMPP001","EMPP002","EMPP003");

        EmployeeDto employee = new EmployeeDto();
        employee.setId("DE0001");
        employee.setLogin("hello_world");
        employee.setStartDate("2021-04-22");
        employee.setName("Helo name");
        employee.setSalary(3360.33);


        when(employeeRepo.getLoginExpIdList(Mockito.anyString())).thenReturn(loginList);
        when(employeeRepo.getIdList()).thenReturn(idList);
        when(employeeRepo.findById(anyString())).thenReturn(Optional.of(employee));

        Assertions.assertEquals(csvValid.isValid(), validationService.isCsvValid(csvList).isValid());
        Assertions.assertEquals(employeeValidDto.isValid(), validationService.isEmployeeCreateValid(employee).isValid());
        Assertions.assertEquals(employeeValidDto.isValid(), validationService.isEmployeePatchValid(employee, "DE0001").isValid());
    }
}
