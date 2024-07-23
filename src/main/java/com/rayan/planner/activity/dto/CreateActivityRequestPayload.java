package com.rayan.planner.activity.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload for creating a activity")
public record CreateActivityRequestPayload(

          @Schema(description = "Title of the activity", example = "Snack at Burger King")
          @NotBlank String title,

          @Schema(description = "Date when the activity will take place", example = "2024-12-01T22:00:00")
          @NotBlank String occurs_at

) {
}
