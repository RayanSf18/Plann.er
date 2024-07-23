package com.rayan.planner.participant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to invite a participant")
public record AddParticipantToTripRequestPayload(

          @Schema(description = "Email of the participant", example = "joe.doe@gmail.com")
          @NotBlank String email

) {
}
