package com.nphc.hr.services;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.repository.EmployeeRepo;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import static com.nphc.hr.utils.Constants.*;

@Service
public class CsvFileService {

    @Autowired
    ValidationService validationService;
    @Autowired
    EmployeeRepo employeeRepo;

    public List<String[]> readCsvFile(MultipartFile file) throws CsvValidationException
    {
        try(Reader reader = new InputStreamReader(file.getInputStream());
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build()){

            String[] nextRecord;
            List<String[]> rawCsvList = new ArrayList<>();
            // we are going to read data line by line
            while ((nextRecord = csvReader.readNext()) != null) {
                if(!nextRecord[0].contains(IGNORED_COMMENT)){
                    rawCsvList.add(nextRecord);
                }
            }
            return rawCsvList;
        } catch (IOException e) {
            throw new CsvValidationException(CSV_ERROR_CODE_05, "Csv file is invalid.");
        }
    }

    private List<EmployeeDto> generateEmployeeList(List<String[]> rawCsvList) throws CsvValidationException {
        List<EmployeeDto> employeeList = new ArrayList<>();
        try {
            CsvValidDto csvValidity = validationService.isCsvValid(rawCsvList);
            if (csvValidity.isValid()) {
                for (String[] employeeInfoArr : rawCsvList) {
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
        } catch (Exception ex){
            throw new CsvValidationException("CSV_ERROR_CODE_08", ex.getMessage());
        }
        return employeeList;
    }

    public void processCsvFile(MultipartFile file) throws CsvValidationException {
        List<String[]> rawCsvList = readCsvFile(file);
        List<EmployeeDto> employeeList = generateEmployeeList(rawCsvList);
        for(EmployeeDto employee : employeeList){
            employeeRepo.save(employee);
        }
    }
}
