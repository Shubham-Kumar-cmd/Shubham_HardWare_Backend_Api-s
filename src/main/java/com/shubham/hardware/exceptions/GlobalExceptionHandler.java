package com.shubham.hardware.exceptions;

import com.shubham.hardware.dtos.ApiResponseMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

//    handler resource not found exception
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException ex){
        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
        ApiResponseMessage response=ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(true)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

//    MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,Object>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex){
        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
        Map<String, Object> response = new HashMap<>();
//        allErrors.stream().forEach(objectError -> {
//            String message = objectError.getDefaultMessage();
//            String field = ((FieldError)objectError).getField();
//            response.put(field, message);
//        });
        allErrors.forEach(objectError -> {
            String message = objectError.getDefaultMessage();
            String field = ((FieldError)objectError).getField();
            response.put(field, message);
        });
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    PropertyReferenceException
//    @ExceptionHandler(PropertyReferenceException.class)
//    public ResponseEntity<Map<String,Object>> handlePropertyReferenceException(PropertyReferenceException ex){
//        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
//        List<ObjectError> allErrors = ex.getBindingResult().getAllErrors();
//        Map<String, Object> response = new HashMap<>();
//        allErrors.forEach(objectError -> {
//            String message = objectError.getDefaultMessage();
//            String field = ((FieldError)objectError).getField();
//            response.put(field, message);
//        });
//        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
//    }

//    handler BadApiRequest
    @ExceptionHandler(BadApiRequestException.class)
    public ResponseEntity<ApiResponseMessage> handlerBadApiRequestException(BadApiRequestException ex){
        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
        ApiResponseMessage response=ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.BAD_REQUEST)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

//    handler NoSuchFileException
    @ExceptionHandler(NoSuchFileException.class)
    public ResponseEntity<ApiResponseMessage> handlerNoSuchFileException(NoSuchFileException ex){
        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
        ApiResponseMessage response=ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

//    handler InvalidPathException
    @ExceptionHandler(InvalidPathException.class)
    public ResponseEntity<ApiResponseMessage> handlerInvalidPathException(InvalidPathException ex){
        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
        ApiResponseMessage response=ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }


//     handler NoSuchElementException
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<ApiResponseMessage> handlerNoSuchElementException(NoSuchElementException ex){
        logger.info("Exception Handler Invoked  : {}",ex.getMessage());
        ApiResponseMessage response=ApiResponseMessage.builder()
                .message(ex.getMessage())
                .success(false)
                .status(HttpStatus.NOT_FOUND)
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }
}
