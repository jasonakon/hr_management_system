package com.nphc.hr;

import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.services.FilterService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = SalaryManagementApplication.class)
@WebAppConfiguration
class FilterServiceTests {

    @InjectMocks
    FilterService filterService;

    @Test
    void testFilterOffset(){
        EmployeeDto employee1 = new EmployeeDto();
        employee1.setId("DE0001");
        employee1.setLogin("hello_world");
        employee1.setStartDate("2021/04/22");
        employee1.setName("Helo name");
        employee1.setSalary(3360.33);

        List<EmployeeDto> employeeList = new ArrayList<>();
        employeeList.add(employee1);
        employeeList.add(employee1);
        employeeList.add(employee1);
        employeeList.add(employee1);

        filterService.filterByLimit(2,employeeList);
        filterService.filterByOffset(2,employeeList);
    }
}
