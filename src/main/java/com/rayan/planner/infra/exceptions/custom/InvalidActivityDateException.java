package com.rayan.planner.infra.exceptions.custom;

import com.rayan.planner.infra.exceptions.handler.PlannerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class InvalidActivityDateException extends PlannerException {

          private final String detail;

          public InvalidActivityDateException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
                    problemDetail.setTitle("The date is invalid");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
