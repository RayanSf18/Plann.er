package com.rayan.planner.infra.exceptions;

import com.rayan.planner.infra.exceptions.handler.PlannerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(PlannerException.class)
    public ProblemDetail handleSimpleEcommerceException(PlannerException exception) {
        return exception.toProblemDetail();
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
        var fieldErrors = exception.getFieldErrors()
            .stream()
            .map(fieldError -> new InvalidParam(fieldError.getField(), fieldError.getDefaultMessage()))
            .toList();

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Your request parameters didn't validate.");
        problemDetail.setProperty("invalid-params", fieldErrors);

        return problemDetail;
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ProblemDetail handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
        var invalidParam = new InvalidParam(exception.getName(), "should be of type " + exception.getRequiredType().getSimpleName());

        ProblemDetail problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problemDetail.setTitle("Path variable type mismatch.");
        problemDetail.setDetail(exception.getMessage());
        problemDetail.setProperty("invalid-params", invalidParam);

        return problemDetail;
    }

    private record InvalidParam(String name, String reason){}
}
