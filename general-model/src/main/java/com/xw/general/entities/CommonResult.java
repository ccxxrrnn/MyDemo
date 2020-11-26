package com.xw.general.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommonResult<T> {
    //404
    private Integer state;
    private String message;
    private T data;

    public CommonResult(Integer state,String message){
        this(state,message,null);
    }

    public CommonResult(Integer state){
        this(state,null,null);
    }
}