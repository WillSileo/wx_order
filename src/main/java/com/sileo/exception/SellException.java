package com.sileo.exception;

import com.sileo.enums.ResultEnum;

public class SellException extends RuntimeException {

    private Integer code;

    public SellException(ResultEnum resultEnum){
        super(resultEnum.getMessage());
        this.code = resultEnum.getCode();
    }

    public SellException(Integer code , String msg){
           super(msg);
           this.code = code;
    }
}
