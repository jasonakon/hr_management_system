package com.nphc.hr.exceptions;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import com.nphc.hr.dto.ErrMsgDto;
import com.nphc.hr.dto.MsgDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.multipart.MultipartException;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
public class ExceptionHandlers {
    @ExceptionHandler(value = CsvValidationException.class)
    public ResponseEntity<Object> exception(CsvValidationException exception){
        return new ResponseEntity<>(new MsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = EmployeeCrudException.class)
    public ResponseEntity<Object> employeeCrudException(EmployeeCrudException exception){
        return new ResponseEntity<>(new MsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Object> paramTypeException(MethodArgumentTypeMismatchException exception){
        return new ResponseEntity<>(new ErrMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MissingServletRequestParameterException.class)
    public ResponseEntity<Object> paramMissingException(MissingServletRequestParameterException exception){
        return new ResponseEntity<>(new ErrMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = UnrecognizedPropertyException.class)
    public ResponseEntity<Object> invalidPropertyException(UnrecognizedPropertyException exception){
        return new ResponseEntity<>(new ErrMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = HttpMessageNotReadableException.class)
    public ResponseEntity<Object> invalidJsonBodyException(HttpMessageNotReadableException exception){
        return new ResponseEntity<>(new ErrMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = ConstraintViolationException.class)
    public ResponseEntity<Object> invalidRequestInputException(ConstraintViolationException exception){
        return new ResponseEntity<>(new ErrMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = MultipartException.class)
    public ResponseEntity<Object> invalidRequestFileException(MultipartException exception){
        return new ResponseEntity<>(new ErrMsgDto(exception.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
