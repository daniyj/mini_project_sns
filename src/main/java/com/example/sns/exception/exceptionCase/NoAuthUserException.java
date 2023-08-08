package com.example.sns.exception.exceptionCase;

import com.example.sns.exception.status.Status403Exception;

public class NoAuthUserException extends Status403Exception {
    public NoAuthUserException() {
        super("권한이 없는 사용자입니다.");
    }
}
