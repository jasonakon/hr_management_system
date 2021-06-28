package com.nphc.hr.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MsgDto {
    private String message;

    public MsgDto(String message){
        this.message = message;
    }
}
