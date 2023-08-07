package com.example.sns.exception.exceptionCase;

import com.example.sns.exception.status.Status400Exception;

public class NotFoundFeedException extends Status400Exception {
    public NotFoundFeedException() {
        super("존재하지 않는 피드입니다.");
    }
}
