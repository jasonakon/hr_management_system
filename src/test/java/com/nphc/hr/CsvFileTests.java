package com.nphc.hr;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.repository.EmployeeRepo;
import com.nphc.hr.services.CsvFileService;
import com.nphc.hr.services.ValidationService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SalaryManagementApplication.class)
@WebAppConfiguration
class CsvFileTests {
    @InjectMocks
    CsvFileService csvFileService;

    @Mock
    EmployeeRepo employeeRepo;

    @Mock
    ValidationService validationService;

    @BeforeEach
    public void init() throws IOException, CsvValidationException {
        EmployeeDto employee = new EmployeeDto();
        employee.setId("DE0001");
        employee.setLogin("hello_world");
        employee.setStartDate("2021/04/22");
        employee.setName("Helo name");
        employee.setSalary(3360.33);

        when(employeeRepo.save(any(EmployeeDto.class))).thenReturn(employee);
    }

    @Test
    void testProcessCsvFile() throws CsvValidationException, IOException {
        String resourceName = "csv/employee_test_5.csv";
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(resourceName).getFile());
        byte[] content = Files.readAllBytes(file.getAbsoluteFile().toPath());
        MultipartFile multipartFile = new MockMultipartFile("data", "filename.csv", "text/plain", content);
        when(validationService.isCsvValid(Mockito.anyList())).thenReturn(new CsvValidDto(true));
        csvFileService.processCsvFile(multipartFile);
    }
}
