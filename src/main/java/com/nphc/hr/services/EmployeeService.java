package com.nphc.hr.services;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.exceptions.CsvValidationException;
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
import java.util.List;

import static com.nphc.hr.utils.Constants.*;

@Service
public class EmployeeService {

    private static final Logger logger = LogManager.getLogger(EmployeeService.class);

    @Autowired
    ValidationService validationService;
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
    

}
