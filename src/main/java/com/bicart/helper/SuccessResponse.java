package com.bicart.helper;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;

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

    public static ResponseEntity<SuccessResponse> setSuccessResponse(String successMessage, HttpStatusCode httpStatusCode, Object object) {
        return ResponseEntity.status(httpStatusCode).body(SuccessResponse.builder()
                .success(true)
                .code(httpStatusCode.value())
                .message(successMessage)
                .response(object)
                .status("SUCCESS")
                .build());
    }

    public static ResponseEntity<SuccessResponse> setSuccessResponse(String successMessage, HttpStatusCode httpStatusCode) {
        return ResponseEntity.status(httpStatusCode).body(SuccessResponse.builder()
                .success(true)
                .code(httpStatusCode.value())
                .message(successMessage)
                .status("SUCCESS")
                .build());
    }
}
