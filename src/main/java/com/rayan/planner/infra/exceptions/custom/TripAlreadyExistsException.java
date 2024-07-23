package com.rayan.planner.infra.exceptions.custom;

import com.rayan.planner.infra.exceptions.handler.PlannerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TripAlreadyExistsException extends PlannerException {

          private final String detail;

          public TripAlreadyExistsException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.FOUND);
                    problemDetail.setTitle("Trip already exists");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
