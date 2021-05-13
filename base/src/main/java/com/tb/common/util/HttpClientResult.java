package com.tb.common.util;

import lombok.AllArgsConstructor;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HttpClientResult {
    private Integer code;
    private String content;

    public HttpClientResult(Integer statusCode) {
        this.code=statusCode;
    }
}
