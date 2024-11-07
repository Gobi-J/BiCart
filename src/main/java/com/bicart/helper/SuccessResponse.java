package com.bicart.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class SuccessResponse {
    private boolean success;
    private int code;
    private String message;
    private String status;
    private Object response;
}
