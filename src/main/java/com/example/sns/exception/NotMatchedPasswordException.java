package com.example.sns.exception;

import com.example.sns.exception.status.Status400Exception;

public class NotMatchedPasswordException extends Status400Exception {
    public NotMatchedPasswordException() {
        super("비밀번호가 일치하지 않습니다.");
    }
}
