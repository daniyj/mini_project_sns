package com.example.sns.exception;

import com.example.sns.exception.status.Status400Exception;

public class DuplicateUsernameException extends Status400Exception {
    public DuplicateUsernameException() {
        super("이미 존재하는 아이디입니다.");
    }
}
