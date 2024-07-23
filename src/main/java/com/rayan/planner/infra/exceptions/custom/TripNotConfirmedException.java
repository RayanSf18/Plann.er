package com.rayan.planner.infra.exceptions.custom;

import com.rayan.planner.infra.exceptions.handler.PlannerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class TripNotConfirmedException extends PlannerException {

          private final String detail;

          public TripNotConfirmedException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.UNPROCESSABLE_ENTITY);
                    problemDetail.setTitle("Trip not confirmed");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
