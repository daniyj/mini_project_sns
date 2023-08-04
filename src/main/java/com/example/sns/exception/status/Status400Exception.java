package com.example.sns.exception.status;

public class Status400Exception extends RuntimeException{
    public Status400Exception(String message) {
        super(message);
    }
}
