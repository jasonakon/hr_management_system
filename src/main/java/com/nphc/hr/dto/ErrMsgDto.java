package com.nphc.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrMsgDto {
    private String errorMsg;

    public ErrMsgDto(String message){
        this.errorMsg = message;
    }
}
