package com.example.sns.exception;

import com.example.sns.exception.status.Status400Exception;

public class NotFoundUsernameException extends Status400Exception {

    public NotFoundUsernameException() {
        super("존재하지 않는 아이디 입니다.");
    }
}
