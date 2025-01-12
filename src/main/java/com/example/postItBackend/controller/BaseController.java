package com.example.postItBackend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

public abstract class BaseController<T> {

    // 성공 시 보여 줄 응답
    protected ResponseEntity<T> createSuccessResponse(T body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    protected ResponseEntity<List<T>> createSuccessResponseList(List<T> body) {
        return new ResponseEntity<>(body, HttpStatus.OK);
    }

    // 실패 시 보여 줄 응답
    protected ResponseEntity<String> createErrorResponse(String message) {
        return new ResponseEntity<>(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }

//    public abstract ResponseEntity<List<T>> getAll();

}
