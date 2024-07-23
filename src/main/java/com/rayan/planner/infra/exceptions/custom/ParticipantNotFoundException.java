package com.rayan.planner.infra.exceptions.custom;

import com.rayan.planner.infra.exceptions.handler.PlannerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ParticipantNotFoundException extends PlannerException {

          private final String detail;

          public ParticipantNotFoundException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
                    problemDetail.setTitle("Participant not found");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
