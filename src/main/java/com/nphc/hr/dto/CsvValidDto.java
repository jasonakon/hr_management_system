package com.nphc.hr.dto;

import lombok.Getter;

@Getter
public class CsvValidDto {

    private boolean isValid;
    private String errCode;
    private String errMsg;

    public CsvValidDto(boolean isValid,String errCode, String errMsg){
        this.isValid = isValid;
        this.errCode = errCode;
        this.errMsg  = errMsg;
    }

    public CsvValidDto(boolean isValid){
        this.isValid = isValid;
    }

}
