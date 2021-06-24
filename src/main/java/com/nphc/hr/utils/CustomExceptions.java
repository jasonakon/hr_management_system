package com.nphc.hr.utils;

public abstract class CustomExceptions extends Exception {

    public CustomExceptions(String errMsg) {
    }

    public class CsvValidationException extends CustomExceptions{
        public CsvValidationException(String errMsg){
            super(errMsg);
        }
    }
}
