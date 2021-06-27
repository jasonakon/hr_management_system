package com.nphc.hr.exceptions;

import com.nphc.hr.dto.CrudMsgDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(value = CsvValidationException.class)
    public ResponseEntity<Object> exception(CsvValidationException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmployeeCrudException.class)
    public ResponseEntity<Object> employeeCrudException(EmployeeCrudException exception){
        return new ResponseEntity<>(new CrudMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
