package com.thatblackbwoy.couponservice.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.function.Supplier;

//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//@Builder
public class BusinessException extends Exception {
//    private String message;
//    private Throwable cause;
    public BusinessException(String message){
        super(message);
    }
    public BusinessException(String message, Throwable cause){
        super(message, cause);
    }


}
