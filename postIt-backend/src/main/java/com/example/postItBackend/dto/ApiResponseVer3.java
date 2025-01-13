package com.example.postItBackend.dto;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponseVer3<T> {
    private String path;
    private boolean success;
    private int status;
    private String message;

    //    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime timestamp;

    private T data;

//    public ApiResponseVer3(int status, String message, T data) {
//        this.status = status;
//        this.message = message;
//        this.timestamp = LocalDateTime.now();
//        this.data = data;
//    }

    public static <T> ApiResponseVer3<T> success(T data, int status, String message) {
        ApiResponseVer3<T> response = new ApiResponseVer3<>();
        response.success = true;
        response.status = status;
        response.message = message;
        response.data = data;
        response.timestamp = LocalDateTime.now();

        return response;
    }

    public static <T> ApiResponseVer3<T> error(String path, int status, String message) {
        ApiResponseVer3<T> response = new ApiResponseVer3<>();
        response.path = path;
        response.success = false;
        response.status = status;
        response.message = message;
        response.timestamp = LocalDateTime.now();

        return response;
    }
}
