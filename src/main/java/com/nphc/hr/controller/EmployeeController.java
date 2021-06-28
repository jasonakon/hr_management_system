package com.nphc.hr.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nphc.hr.dto.MsgDto;
import com.nphc.hr.dto.EmployeeDto;
import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.exceptions.EmployeeCrudException;
import com.nphc.hr.services.CsvFileService;
import com.nphc.hr.services.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Min;

@RestController
@Validated
public class EmployeeController {
    public static final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    EmployeeService employeeService;
    @Autowired
    CsvFileService csvFileService;

    @PostMapping(value="/users/upload")
    public ResponseEntity<Object> fileUpload(@RequestPart(value = "file")MultipartFile file) throws CsvValidationException {
        csvFileService.processCsvFile(file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // API CRUD:
    @GetMapping(value = "/users")
    public ResponseEntity<Object> getUsers(@RequestParam(value = "minSalary", required = false, defaultValue = "0") @Min(0) double minSalary,
                                           @RequestParam(value = "maxSalary", required = false, defaultValue = "4000.00") @Min(0) double maxSalary,
                                           @RequestParam(value = "offset", required = false, defaultValue = "0") @Min(0) int offset,
                                           @RequestParam(value = "limit", required = false, defaultValue = "0") @Min(0) int limit,
                                           @RequestParam(value = "id", required = false, defaultValue = "empty") String id) throws EmployeeCrudException {
        Object fetchResult = employeeService.processEmpFetch(minSalary, maxSalary, offset, limit, id);
        return new ResponseEntity<>(fetchResult, HttpStatus.OK);
    }

    @PostMapping(value = "/users")
    public ResponseEntity<Object> createUser(@RequestBody String reqJson) throws EmployeeCrudException, JsonProcessingException {
        EmployeeDto reqEmployee = new ObjectMapper().readValue(reqJson, EmployeeDto.class);
        employeeService.createEmployee(reqEmployee);
        return new ResponseEntity<>(new MsgDto("Successfully created"), HttpStatus.CREATED);
    }

    @PutMapping(value = "/users")
    public ResponseEntity<Object> updateUser(@RequestParam(value = "id") String id,
                                            @RequestBody String reqJson) throws EmployeeCrudException, JsonProcessingException {
        EmployeeDto reqEmployee = new ObjectMapper().readValue(reqJson, EmployeeDto.class);
        employeeService.patchEmployee(reqEmployee, id);
        return new ResponseEntity<>(new MsgDto("Successfully updated"), HttpStatus.OK);
    }

    @DeleteMapping(value = "/users")
    public ResponseEntity<Object> deleteUser(@RequestParam(value = "id") String id) throws EmployeeCrudException {
        employeeService.deleteEmployee(id);
        return new ResponseEntity<>(new MsgDto("Successfully deleted"), HttpStatus.OK);
    }
}
