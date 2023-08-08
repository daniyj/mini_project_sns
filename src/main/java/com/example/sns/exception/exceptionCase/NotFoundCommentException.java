package com.example.sns.exception.exceptionCase;

import com.example.sns.exception.status.Status400Exception;
import com.example.sns.exception.status.Status404Exception;

public class NotFoundCommentException extends Status400Exception {
    public NotFoundCommentException() {
        super("존재하지 않는 댓글입니다.");
    }
}
