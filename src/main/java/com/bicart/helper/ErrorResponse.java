package com.bicart.helper;

import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {
    private boolean success;
    private int code;
    private String errorMessage;
    private String status;

    public static ResponseEntity<ErrorResponse> setErrorResponse(String errorMessage, HttpStatusCode httpStatusCode) {
        return ResponseEntity.status(httpStatusCode).body(ErrorResponse.builder()
                .success(false)
                .code(httpStatusCode.value())
                .errorMessage(errorMessage)
                .status("ERROR")
                .build());
    }
}
