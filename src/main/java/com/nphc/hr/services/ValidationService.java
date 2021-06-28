package com.nphc.hr.services;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.dto.EmployeeValidDto;
import com.nphc.hr.repository.EmployeeRepo;
import com.nphc.hr.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.nphc.hr.utils.Constants.*;

@Service
public class ValidationService {

    @Autowired
    EmployeeRepo employeeRepo;

    private String generateErrMsg(String errMsg, String id){
        return (errMsg + " - Record ID : " + id);
    }

    private boolean isStartDateValid(String dateString){
        try{
            SimpleDateFormat dateFormat1 = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat dateFormat2 = new SimpleDateFormat("dd-MMM-yy");
            String[] splitArray = dateString.split("-");
            if(splitArray[1].length() == 2){
                dateFormat1.parse(dateString);
            } else if(splitArray[1].length() == 3){
                dateFormat2.parse(dateString);
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
        return true;
    }

    private boolean isSalaryValid(double salary){
        return (salary >= 0.0);
    }

    private boolean isRecordEmpty(String[] employeeInfoArr){
        for(String employeeInfo : employeeInfoArr){
            if(employeeInfo.isEmpty()){
                return true;
            }
        }
        return false;
    }

    private boolean isIdLoginUnique(List<String> idList , List<String> loginList, String id, String login){
        if(idList.contains(id) || loginList.contains(login)){
            return false;
        }
        idList.add(id);
        loginList.add(login);
        return true;
    }

    private boolean isLoginTableUnique(String login, String id){
        List<String> tableLoginList = employeeRepo.getLoginExpIdList(id);
        return (!tableLoginList.contains(login));
    }

    private boolean isIdTableUnique(String id){
        List<String> tableIdList = employeeRepo.getIdList();
        return (!tableIdList.contains(id));
    }

    private boolean isIdTableExist(String id){
        Optional<EmployeeDto> employeeOpt = employeeRepo.findById(id);
        return employeeOpt.isPresent();
    }

    public CsvValidDto isCsvValid(List<String[]> employeeInfoList){
        List<String> idList    = new ArrayList<>();
        List<String> loginList = new ArrayList<>();

        try {
            for (String[] employeeInfoArr : employeeInfoList) {
                String id = employeeInfoArr[Constants.CSV_ID_INDEX];
                String login = employeeInfoArr[Constants.CSV_LOGIN_INDEX];
                String salaryStr = employeeInfoArr[Constants.CSV_SALARY_INDEX];
                String startDate = employeeInfoArr[Constants.CSV_START_DATE_INDEX];
                double salary = Double.parseDouble(salaryStr);

                if (isRecordEmpty(employeeInfoArr)) {
                    return new CsvValidDto(false, CSV_ERROR_CODE_01, generateErrMsg("One or more columns are empty", id));
                } else if (!isIdLoginUnique(idList, loginList, id, login)) {
                    return new CsvValidDto(false, CSV_ERROR_CODE_02, generateErrMsg("Either ID or LOGIN is not unique", id));
                } else if (!isLoginTableUnique(login, id)) {
                    return new CsvValidDto(false, CSV_ERROR_CODE_06, generateErrMsg("Login in not unique in table set", id));
                } else if (!isSalaryValid(salary)) {
                    return new CsvValidDto(false, CSV_ERROR_CODE_03, generateErrMsg("Salary is not more or equal than 0.0", id));
                } else if (!isStartDateValid(startDate)) {
                    return new CsvValidDto(false, CSV_ERROR_CODE_04, generateErrMsg("Date is invalid", id));
                }
            }
            return new CsvValidDto(true);
        } catch (ArrayIndexOutOfBoundsException exception){
            return new CsvValidDto(false, CSV_ERROR_CODE_07, "The CSV data format does not comply with the given template");
        }
    }

    public EmployeeValidDto isEmployeeCreateValid(EmployeeDto employeeDto){
        if(!isIdTableUnique(employeeDto.getId())){
            return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_01, API_CRUD_ERROR_CODE_01_MSG);
        } else if(!isLoginTableUnique(employeeDto.getLogin(), employeeDto.getId())){
            return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_02, API_CRUD_ERROR_CODE_02_MSG);
        } else if(!isSalaryValid(employeeDto.getSalary())){
            return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_03, API_CRUD_ERROR_CODE_03_MSG);
        } else if(!isStartDateValid(employeeDto.getStartDate())){
            return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_04, API_CRUD_ERROR_CODE_04_MSG);
        }
        return new EmployeeValidDto(true);
    }

    public EmployeeValidDto isEmployeePatchValid(EmployeeDto employeeDto, String id){
        if(employeeDto.getId().equals(id)){
            if (!isIdTableExist(id)) {
                return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_05, API_CRUD_ERROR_CODE_05_MSG);
            } else if (!isLoginTableUnique(employeeDto.getLogin(), employeeDto.getId())) {
                return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_02, API_CRUD_ERROR_CODE_02_MSG);
            } else if (!isSalaryValid(employeeDto.getSalary())) {
                return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_03, API_CRUD_ERROR_CODE_03_MSG);
            } else if (!isStartDateValid(employeeDto.getStartDate())) {
                return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_04, API_CRUD_ERROR_CODE_04_MSG);
            }
            return new EmployeeValidDto(true);
        } else {
            return new EmployeeValidDto(false, API_CRUD_ERROR_CODE_06, API_CRUD_ERROR_CODE_06_MSG);
        }
    }
}
