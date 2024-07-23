package com.rayan.planner.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(description = "Payload for update a trip")
public record UpdateTripRequestPayload(

          @Schema(description = "Destination of the trip", example = "New York, EUA")
          @NotBlank String destiny,

          @Schema(description = "Start date and time of the trip", example = "2024-12-01T22:00:00")
          @NotBlank String startsAt,

          @Schema(description = "End date and time of the trip", example = "2024-12-07T22:00:00")
          @NotBlank String endsAt

) {
}
