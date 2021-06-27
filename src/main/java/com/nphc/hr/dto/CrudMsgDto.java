package com.nphc.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrudMsgDto {
    private String message;

    public CrudMsgDto(String message){
        this.message = message;
    }
}
