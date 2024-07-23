package com.rayan.planner.participant;

import com.rayan.planner.infra.exceptions.custom.ParticipantAlreadyExistsException;
import com.rayan.planner.infra.exceptions.custom.ParticipantNotFoundException;
import com.rayan.planner.infra.exceptions.custom.TripNotConfirmedException;
import com.rayan.planner.infra.exceptions.custom.TripNotFoundException;
import com.rayan.planner.participant.dto.ConfirmParticipantRequestPayload;
import com.rayan.planner.trip.Trip;
import com.rayan.planner.trip.TripService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service
public class ParticipantService {

    private final ParticipantRepository participantRepository;
    private final TripService tripService;

    public ParticipantService(ParticipantRepository participantRepository, @Lazy TripService tripService) {
        this.participantRepository = participantRepository;
        this.tripService = tripService;
    }

    public void addParticipantsToTrip(Set<String> emails, Trip trip) {
        List<Participant> newParticipants = emails.stream().map(email -> new Participant(email, trip)).toList();
        participantRepository.saveAll(newParticipants);
    }

    public UUID addParticipantToTrip(String email, Trip trip) {

        Optional<Participant> optionalParticipant = participantRepository.findByEmail(email);

        if (optionalParticipant.isPresent()) {
            throw new ParticipantAlreadyExistsException("There is already a participant with this email in the trip.");
        }

        Participant newParticipant = new Participant(email, trip);

        participantRepository.save(newParticipant);

        return newParticipant.getId();
    }

    public Participant confirmParticipant(UUID participantId, ConfirmParticipantRequestPayload payload) {
        Participant participantDb = participantRepository.findById(participantId).orElseThrow(() -> new ParticipantNotFoundException("Participant with ID " + participantId + " was not found."));

        if (!participantDb.getTrip().getConfirmed()) {
            throw new TripNotConfirmedException("It is not possible to confirm the participant on a trip that has not yet been confirmed.");
        }

        participantDb.setName(payload.name());
        participantDb.setConfirmed(true);

        participantRepository.save(participantDb);

        return participantDb;
    }

    public List<Participant> getAllParticipantsFromTrip(UUID tripId) {
        try {
            tripService.getTrip(tripId);
            return participantRepository.findByTripId(tripId);
        } catch (TripNotFoundException exception) {
            throw exception;
        }
    }

    public void triggerConfirmationEmailToParticipants(UUID tripId) {}

    public void triggerConfirmationEmailToParticipant(String email) {}

}
