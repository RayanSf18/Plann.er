package com.rayan.planner.trip.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.Set;

@Schema(description = "Payload for creating a trip")
public record CreateTripRequestPayload(

    @Schema(description = "Destination of the trip", example = "New York, EUA")
    @NotBlank String destiny,

    @Schema(description = "Start date and time of the trip", example = "2024-12-01T22:00:00")
    @NotBlank String startsAt,

    @Schema(description = "End date and time of the trip", example = "2024-12-07T22:00:00")
    @NotBlank String endsAt,

    @Schema(description = "Name of the trip owner", example = "John Doe")
    @NotBlank String ownerName,

    @Schema(description = "Email of the trip owner", example = "john.doe@example.com")
    @NotBlank String ownerEmail,

    @Schema(description = "List of participant emails", example = "[\"bob.smith@example.com\", \"alice.johnson@example.com\"]")
    @NotNull Set<String> emails

) {}
