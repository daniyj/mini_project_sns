package com.example.sns.exception.exceptionCase;

import com.example.sns.exception.status.Status500Exception;

public class ImageUpdateException extends Status500Exception {

    public ImageUpdateException() {
        super("이미지 등록 과정에 문제가 발생했습니다.");
    }
}
