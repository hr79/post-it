package com.example.postItBackend.common.response;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponse<T> {
    private String path;
    private boolean success;
    private int status;
    private String message;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private T data;

    public static <T> ApiResponse<T> success(T data, int status, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.success = true;
        response.status = status;
        response.message = message;
        response.data = data;
        response.timestamp = LocalDateTime.now();

        return response;
    }

    public static <T> ApiResponse<T> error(String path, int status, String message) {
        ApiResponse<T> response = new ApiResponse<>();
        response.path = path;
        response.success = false;
        response.status = status;
        response.message = message;
        response.timestamp = LocalDateTime.now();

        return response;
    }
}
