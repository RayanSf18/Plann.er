package com.rayan.planner.trip.dto;

import com.rayan.planner.trip.Trip;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

public record TripResponsePayload(

    UUID trip_id,
    String destiny,
    LocalDateTime starts_at,
    LocalDateTime ends_at,
    String owner_name,
    String owner_email,
    Boolean confirmed
) {

    public static TripResponsePayload toTripResponse(Trip trip) {
        return new TripResponsePayload(trip.getId(), trip.getDestiny(), trip.getStartsAt(), trip.getEndsAt(), trip.getOwnerName(), trip.getOwnerEmail(),trip.getConfirmed());
    }

}
