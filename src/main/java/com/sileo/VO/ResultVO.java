package com.sileo.VO;

import lombok.Data;

import java.io.Serializable;

@Data
public class ResultVO<T> implements Serializable {

    /* 错误码 */
    private Integer code;
    /* 错误信息 */
    private String msg;
    /* 返回数据 */
    private T data;
}
