package com.act.Gakos.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseResponse<T> {
    private String message;
    private boolean success;
    private T data;

    public BaseResponse(String message, boolean success) {
        this.message = message;
        this.success = success;
    }
}
