package com.example.sns.exception.exceptionCase;

import com.example.sns.exception.status.Status403Exception;

public class NoAuthUserException extends Status403Exception {
    public NoAuthUserException() {
        super("해당 기능에 대해 권한이 없습니다.");
    }
}
