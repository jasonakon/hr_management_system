package com.nphc.hr.dto;

import lombok.Getter;

@Getter
public class EmployeeValidDto {
    private boolean isValid;
    private String errCode;
    private String errMsg;

    public EmployeeValidDto(boolean isValid,String errCode, String errMsg){
        this.isValid = isValid;
        this.errCode = errCode;
        this.errMsg  = errMsg;
    }

    public EmployeeValidDto(boolean isValid){
        this.isValid = isValid;
    }
}
