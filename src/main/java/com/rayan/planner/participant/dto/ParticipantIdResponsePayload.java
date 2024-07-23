package com.rayan.planner.participant.dto;

import com.rayan.planner.participant.Participant;
import com.rayan.planner.trip.Trip;

import java.util.UUID;

public record ParticipantIdResponsePayload(

    UUID participant_id

) {

}
