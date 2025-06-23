package com.example.postItBackend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {
//    private boolean success;
    private int status;
    private String message;

//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private T data;

    public ApiResponse(int status, String message, T data) {
        this.status = status;
        this.message = message;
        this.timestamp = LocalDateTime.now();
        this.data = data;
    }

//    public static <T> ApiResponse<T> success(T data, int status, String message) {
//        ApiResponse<T> response = new ApiResponse<>();
//        response.success = true;
//        response.status = status;
//        response.message = message;
//        response.data = data;
//        response.timestamp = LocalDateTime.now();
//
//        return response;
//    }
//
//    public static <T> ApiResponse<T> error(int status, String message) {
//        ApiResponse<T> response = new ApiResponse<>();
//        response.success = false;
//        response.status = status;
//        response.message = "ERROR";
//        response.timestamp = LocalDateTime.now();
//
//        return response;
//    }
}
