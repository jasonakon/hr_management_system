package com.nphc.hr.services;

import com.nphc.hr.dto.CsvValidDto;
import com.nphc.hr.utils.Constants;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import static com.nphc.hr.utils.Constants.*;

@Service
public class ValidationService {

    public boolean isStartDateValid(String dateString){
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

    public boolean isSalaryValid(String salaryStr){
        double salary = Double.parseDouble(salaryStr);
        return (salary >= 0.0);
    }

    public boolean isRecordEmpty(String[] employeeInfoArr){
        for(String employeeInfo : employeeInfoArr){
            if(employeeInfo.isEmpty()){
                return true;
            }
        }
        return false;
    }

//    public boolean isRecordIgnored(){
//
//    }

    public boolean isIdLoginUnique(List<String> idList , List<String> loginList, String id, String login){
        if(idList.contains(id) || loginList.contains(login)){
            return false;
        }

        idList.add(id);
        loginList.add(login);
        return true;
    }

    public CsvValidDto isCsvValid(List<String[]> employeeInfoList){
        List<String> idList    = new ArrayList<>();
        List<String> loginList = new ArrayList<>();

        for( String[] employeeInfoArr : employeeInfoList){
            String id = employeeInfoArr[Constants.CSV_ID_INDEX];
            String login = employeeInfoArr[Constants.CSV_LOGIN_INDEX];
            String salary = employeeInfoArr[Constants.CSV_SALARY_INDEX];
            String startDate = employeeInfoArr[Constants.CSV_START_DATE_INDEX];

            if(isRecordEmpty(employeeInfoArr)){
                return new CsvValidDto(false, CSV_ERROR_CODE_01, "One or more fields are empty in the record set.");
            } else if(!isIdLoginUnique(idList, loginList, id, login)){
                return new CsvValidDto(false, CSV_ERROR_CODE_02, "Either ID or LOGIN is not unique in the record set.");
            } else if(!isSalaryValid(salary)){
                return new CsvValidDto(false, CSV_ERROR_CODE_03, "Salary is not more or equal than 0.0 in the record set.");
            } else if(!isStartDateValid(startDate)){
                return new CsvValidDto(false, CSV_ERROR_CODE_04, "startDate is invalid in record set.");
            }
        }
        return new CsvValidDto(true);
    }
}
