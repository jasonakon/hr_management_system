package com.nphc.hr.services;

import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.dto.EmployeeValidDto;
import com.nphc.hr.dto.ResultsDto;
import com.nphc.hr.exceptions.EmployeeCrudException;
import com.nphc.hr.repository.EmployeeRepo;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

import static com.nphc.hr.utils.Constants.*;

@Service
public class EmployeeService {

    private static final Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    ValidationService validationService;
    @Autowired
    FilterService filterService;
    @Autowired
    EmployeeRepo employeeRepo;

    public EmployeeDto getEmployeeById(String id) throws EmployeeCrudException {
        Optional<EmployeeDto> employee = employeeRepo.findById(id);
        Supplier<EmployeeCrudException> exceptionSupplier = () -> new EmployeeCrudException(API_CRUD_ERROR_CODE_05, API_CRUD_ERROR_CODE_05_MSG);
        return employee.orElseThrow(exceptionSupplier);
    }

    public Object processEmpFetch(double minSalary, double maxSalary, int offset, int limit, String id) throws EmployeeCrudException {
        if(!id.equals("empty")){
            return getEmployeeById(id);
        } else {
            List<EmployeeDto> employeeFetchList = employeeRepo.getFetchList(minSalary, maxSalary);
            if (!employeeFetchList.isEmpty()) {
                filterService.filterByOffset(offset, employeeFetchList);
                filterService.filterByLimit(limit, employeeFetchList);
            }
            ResultsDto results = new ResultsDto();
            results.setResults(employeeFetchList);
            return results;
        }
    }

    public void createEmployee(EmployeeDto employee) throws EmployeeCrudException {
        EmployeeValidDto employeeValidity = validationService.isEmployeeCreateValid(employee);
        if(employeeValidity.isValid()){
           employeeRepo.save(employee);
        } else {
            throw new EmployeeCrudException(employeeValidity.getErrCode(), employeeValidity.getErrMsg());
        }
    }

    public void patchEmployee(EmployeeDto employee, String id) throws EmployeeCrudException {
        EmployeeValidDto employeeValidity = validationService.isEmployeePatchValid(employee, id);
        if(employeeValidity.isValid()){
            employeeRepo.save(employee);
        } else {
            throw new EmployeeCrudException(employeeValidity.getErrCode(), employeeValidity.getErrMsg());
        }
    }

    public void deleteEmployee(String id) throws EmployeeCrudException {
        Optional<EmployeeDto> employeeOpt = employeeRepo.findById(id);
        if(employeeOpt.isPresent()){
            employeeRepo.deleteById(id);
        } else {
            throw new EmployeeCrudException(API_CRUD_ERROR_CODE_05, API_CRUD_ERROR_CODE_05_MSG);
        }
    }

}
