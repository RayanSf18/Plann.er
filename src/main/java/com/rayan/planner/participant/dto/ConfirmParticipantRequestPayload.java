package com.rayan.planner.participant.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for confirm a participant to trip")
public record ConfirmParticipantRequestPayload(

          @Schema(description = "Name of the participant", example = "Joe Doe")
          @NotBlank String name

) {
}
