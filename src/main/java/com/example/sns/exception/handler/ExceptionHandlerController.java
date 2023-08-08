package com.example.sns.exception.handler;

import com.example.sns.dto.ResponseDto;
import com.example.sns.exception.status.Status400Exception;
import com.example.sns.exception.status.Status403Exception;
import com.example.sns.exception.status.Status404Exception;
import com.example.sns.exception.status.Status500Exception;
import io.jsonwebtoken.MalformedJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;

@RestControllerAdvice
public class ExceptionHandlerController {

    @ExceptionHandler(Status400Exception.class)
    public ResponseEntity<ResponseDto> exception400(Status400Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @ExceptionHandler(Status404Exception.class)
    public ResponseEntity<ResponseDto> exception404(Status404Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Status500Exception.class)
    public ResponseEntity<ResponseDto> exception500(Status500Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
    @ExceptionHandler(Status403Exception.class)
    public ResponseEntity<ResponseDto> exception403(Status403Exception exception) {
        ResponseDto response = new ResponseDto();
        response.setMessage(exception.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    // (보류) 잘못된 토큰으로 로그인할 시 에러 메시지 출력
//    @ExceptionHandler(MalformedJwtException.class)
//    public ResponseEntity<ResponseDto> exceptionJwtValidation(MalformedJwtException exception) {
//        ResponseDto response = new ResponseDto();
//        response.setMessage("유효하지 않은 토큰입니다. 다시 로그인해주세요.");
//        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
//    }
}
