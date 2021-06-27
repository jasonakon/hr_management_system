package com.nphc.hr.exceptions;

import lombok.Getter;

@Getter
public class EmployeeCrudException extends Exception {
    private String errCode;

    public EmployeeCrudException(String errCode, String errMsg){
        super(errMsg);
        this.errCode = errCode;
    }
}
