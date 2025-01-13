package com.example.postItBackend.common;

import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ApiResponseVer2<T> {
    private String status;
    private T body;
    private final LocalDateTime timestamp;

    public ApiResponseVer2(String status, T body) {
        this.status = status;
        this.body = body;
        this.timestamp = LocalDateTime.now();
    }
}

