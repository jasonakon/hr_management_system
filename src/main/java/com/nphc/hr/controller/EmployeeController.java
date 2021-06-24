package com.nphc.hr.controller;

import com.nphc.hr.exceptions.CsvValidationException;
import com.nphc.hr.services.EmployeeService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EmployeeController {
    public static final Logger logger = LogManager.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeService employeeService;

    @PostMapping(value="/users/upload")
    public ResponseEntity<Object> fileUpload(@RequestPart(value = "file")MultipartFile file) throws CsvValidationException {
        employeeService.processCsvFile(file);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
}
