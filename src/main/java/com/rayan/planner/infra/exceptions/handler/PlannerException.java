package com.rayan.planner.infra.exceptions.handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class PlannerException extends RuntimeException {

    public ProblemDetail toProblemDetail() {
        var problemDetail = ProblemDetail.forStatus(HttpStatus.INTERNAL_SERVER_ERROR);
        problemDetail.setTitle("Planner internal server error.");
        return problemDetail;
    }
}
