package com.bank.application.handler;

import com.bank.application.exceptions.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;


@ControllerAdvice
public class RestControllerAdvice {

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleAllExceptions(HttpServletRequest request, UsernameNotFoundException unfe) {
        return new ResponseEntity<>(
                new CustomException(unfe.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(UsernameAlreadyExistsException.class)
    public ResponseEntity<?> handleAllExceptions(HttpServletRequest request, UsernameAlreadyExistsException uae) {
        return new ResponseEntity<>(
                new CustomException(uae.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidPasswodException.class)
    public ResponseEntity<?> handleAllExceptions(HttpServletRequest request, InvalidPasswodException ipe) {
        return new ResponseEntity<>(
                new CustomException("Invalid password!", HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyLoggedException.class)
    public ResponseEntity<?> handleAllExceptions(HttpServletRequest request, UserAlreadyLoggedException ual) {
        return new ResponseEntity<>(
                new CustomException(ual.getMessage(), HttpStatus.BAD_REQUEST),
                HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(SessionNotFoundException.class)
    public ResponseEntity<?> handleAllExceptions(HttpServletRequest request, SessionNotFoundException anfe) {
        return new ResponseEntity<>(
                new CustomException(anfe.getMessage(), HttpStatus.NOT_FOUND),
                HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<CustomException> handleAllExceptions(HttpServletRequest request, Exception ex) {
        return new ResponseEntity<>(
                new CustomException("An error occurred. Please try again", HttpStatus.INTERNAL_SERVER_ERROR),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
