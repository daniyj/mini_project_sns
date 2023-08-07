package com.example.sns.exception.exceptionCase;

import com.example.sns.exception.status.Status400Exception;

public class NotMatchedUserException extends Status400Exception {
    public NotMatchedUserException() {
        super("피드와 유저 정보가 일치하지 않습니다.");
    }
}
