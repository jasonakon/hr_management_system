package com.nphc.hr.exceptions;

import lombok.Getter;

@Getter
public class CsvValidationException extends Exception {

    private String errCode;

    public CsvValidationException(String errCode, String errMsg){
        super(errMsg);
        this.errCode = errCode;
    }

}
