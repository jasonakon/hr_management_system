package com.nphc.hr.services;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.dto.EmployeeValidDto;
import com.nphc.hr.dto.ResultsDto;
import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.exceptions.EmployeeCrudException;
import com.nphc.hr.repository.EmployeeRepo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
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

    public List<EmployeeDto> readCsvFile(MultipartFile file) throws CsvValidationException
    {
        try(Reader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()){

            String[] nextRecord;
            List<String[]> rawCsvList = new ArrayList<>();
            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                rawCsvList.add(nextRecord);
            }

            List<EmployeeDto> employeeList = new ArrayList<>();
            CsvValidDto csvValidity = validationService.isCsvValid(rawCsvList);
            if(csvValidity.isValid()){
                for(String[] employeeInfoArr : rawCsvList) {
                    EmployeeDto employee = new EmployeeDto();
                    employee.setId(employeeInfoArr[CSV_ID_INDEX]);
                    employee.setLogin(employeeInfoArr[CSV_LOGIN_INDEX]);
                    employee.setName(employeeInfoArr[CSV_NAME_INDEX]);
                    employee.setSalary(Double.parseDouble(employeeInfoArr[CSV_SALARY_INDEX]));
                    employee.setStartDate(employeeInfoArr[CSV_START_DATE_INDEX]);
                    employeeList.add(employee);
                }
            } else {
                throw new CsvValidationException(csvValidity.getErrCode(), csvValidity.getErrMsg());
            }

            return employeeList;
        } catch (IOException e) {
            throw new CsvValidationException(CSV_ERROR_CODE_05, "Csv file is invalid.");
        }
    }

    public void processCsvFile(MultipartFile file) throws CsvValidationException {
        List<EmployeeDto> employeeList = readCsvFile(file);
        for(EmployeeDto employee : employeeList){
            employeeRepo.save(employee);
        }
    }

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

    public void patchEmployee(EmployeeDto employee) throws EmployeeCrudException {
        EmployeeValidDto employeeValidity = validationService.isEmployeePatchValid(employee);
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
