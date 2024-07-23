package com.rayan.planner.participant.dto;

import com.rayan.planner.participant.Participant;
import com.rayan.planner.trip.Trip;

import java.util.UUID;

public record ParticipantResponsePayload(

    UUID participant_id,
    Boolean confirmed,
    String name,
    String email
) {
    public static ParticipantResponsePayload toParticipantResponse(Participant participant) {
        return new ParticipantResponsePayload(participant.getId(), participant.getConfirmed(), participant.getName(), participant.getEmail());
    }
}
