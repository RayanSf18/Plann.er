package com.rayan.planner.infra.exceptions.custom;

import com.rayan.planner.infra.exceptions.handler.PlannerException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;

public class ParticipantAlreadyExistsException extends PlannerException {

          private final String detail;

          public ParticipantAlreadyExistsException(String detail) {
                    this.detail = detail;
          }

          @Override
          public ProblemDetail toProblemDetail() {
                    var problemDetail = ProblemDetail.forStatus(HttpStatus.CONFLICT);
                    problemDetail.setTitle("Participant already exists");
                    problemDetail.setDetail(detail);
                    return problemDetail;
          }

}
