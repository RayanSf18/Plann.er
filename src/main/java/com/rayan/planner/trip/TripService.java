package com.rayan.planner.trip;

import com.rayan.planner.infra.exceptions.custom.*;
import com.rayan.planner.participant.ParticipantService;
import com.rayan.planner.participant.dto.AddParticipantToTripRequestPayload;
import com.rayan.planner.trip.dto.CreateTripRequestPayload;
import com.rayan.planner.trip.dto.UpdateTripRequestPayload;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class TripService {

          private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
          private final TripRepository tripRepository;
          private final ParticipantService participantService;

          public UUID createTrip(CreateTripRequestPayload payload) {

                    checkIfTheTripExists(payload.destiny(), payload.startsAt());

                    validateTripDate(payload.startsAt(), payload.endsAt());

                    Trip newTrip = buildTripFromPayload(payload);

                    log.debug("Creating trip with details: {}", newTrip);

                    tripRepository.save(newTrip);

                    log.debug("Saved trip with ID: {}", newTrip.getId());

                    if (!payload.emails().isEmpty()) {
                              participantService.addParticipantsToTrip(payload.emails(), newTrip);
                    }
                    return newTrip.getId();
          }

          public Trip getTrip(UUID tripId) {
                    return tripRepository.findById(tripId).orElseThrow(() -> new TripNotFoundException("Trip with ID " + tripId + " was not found."));
          }

          public List<Trip> getAllTrips(String startsAt, String endsAt) {

                    List<Trip> trips = tripRepository.findAll(Sort.by(Sort.Direction.ASC, "startsAt"));

                    if (startsAt == null || endsAt == null) return trips;

                    checkIfTheTripDateIsValid(startsAt, endsAt);

                    trips = trips.stream()
                              .filter(trip -> (trip.getStartsAt().isBefore(LocalDateTime.parse(endsAt))))
                              .toList();

                    return trips;
          }

          public Trip confirmTrip(UUID tripId) {
                    Trip tripDb = getTrip(tripId);
                    tripDb.setConfirmed(true);
                    tripRepository.save(tripDb);
                    participantService.triggerConfirmationEmailToParticipants(tripId);
                    return tripDb;
          }

          public Trip updateTrip(UUID tripId, UpdateTripRequestPayload payload) {
                    Trip tripDb = getTrip(tripId);
                    validateTripDate(payload.startsAt(), payload.endsAt());
                    updateTripData(tripDb, payload);
                    tripRepository.save(tripDb);
                    return tripDb;
          }

          public void deleteTrip(UUID tripId) {
                    Trip tripDb = getTrip(tripId);
                    tripRepository.deleteById(tripDb.getId());
          }

          public UUID inviteParticipant(UUID tripId, AddParticipantToTripRequestPayload payload) {
                    Trip trip = getTrip(tripId);

                    validateIfTripIsConfirmed(trip);

                    try {
                              UUID participantId = participantService.addParticipantToTrip(payload.email(), trip);
                              participantService.triggerConfirmationEmailToParticipant(payload.email());
                              return participantId;
                    } catch (ParticipantAlreadyExistsException exception) {
                              throw exception;
                    }
          }

          // Auxiliary methods

          public void validateIfTripIsConfirmed(Trip trip) {
                    if (!trip.getConfirmed()) {
                              throw new TripNotConfirmedException("It is not possible to confirm the participant on a trip that has not yet been confirmed.");
                    }
          }

          private void validateTripDate(String payloadStartsAt, String payloadEndsAt) {
                    checkIfTheTripDateIsValid(payloadStartsAt, payloadEndsAt);
                    checkForTripDateConflicts(payloadStartsAt, payloadEndsAt);
          }

          private void checkIfTheTripExists(String destiny, String startsAt) {
                    LocalDateTime localDateTime = LocalDateTime.parse(startsAt);
                    boolean exists = tripRepository.existsByDestinyAndStartsAt(destiny, localDateTime);
                    if (exists) throw new TripAlreadyExistsException("There is already a trip with this destination with the same starts at.");
          }

          private void checkIfTheTripDateIsValid(String payloadStartsAt, String payloadEndsAt) {
                    try {
                              LocalDateTime startsAt = LocalDateTime.parse(payloadStartsAt, DATE_TIME_FORMATTER);
                              LocalDateTime endsAt = LocalDateTime.parse(payloadEndsAt, DATE_TIME_FORMATTER);

                              if (!endsAt.isAfter(startsAt)) throw new InvalidTripDateException("The end date must be after the start date.");
                              if (startsAt.isBefore(LocalDateTime.now())) throw new InvalidTripDateException("The start date cannot be in the past.");

                    } catch (DateTimeParseException e) {
                              throw new InvalidTripDateException("Invalid date format. Please use yyyy-MM-dd'T'HH:mm:ss format.");
                    }
          }

          private void checkForTripDateConflicts(String startsAt, String endsAt) {
                    LocalDateTime startsAtDateTime = LocalDateTime.parse(startsAt, DATE_TIME_FORMATTER);
                    LocalDateTime endsAtDateTime = LocalDateTime.parse(endsAt, DATE_TIME_FORMATTER);

                    Optional<Trip> conflictingTrip = tripRepository.findConflictingTrip(startsAtDateTime, endsAtDateTime);
                    conflictingTrip.ifPresent(tripDb -> {
                              if (startsAtDateTime.isEqual(tripDb.getStartsAt())) {
                                        throw new TripDateConflictException("The beginning of this trip coincides with the existing trip to: " + tripDb.getDestiny() + " on the date of " + tripDb.getStartsAt());
                              }
                              if (endsAtDateTime.isEqual(tripDb.getEndsAt())) {
                                        throw new TripDateConflictException("The ends of this trip coincides with the existing trip to: " + tripDb.getDestiny() + " on the date of " + tripDb.getEndsAt());
                              }
                              if (endsAtDateTime.isEqual(tripDb.getStartsAt())) {
                                        throw new TripDateConflictException("The ends of this trip coincides with the existing trip to: " + tripDb.getDestiny() + " on the date of " + tripDb.getStartsAt());
                              }
                              if (startsAtDateTime.isAfter(tripDb.getStartsAt()) && endsAtDateTime.isBefore(tripDb.getEndsAt())) {
                                        throw new TripDateConflictException("It is not possible to create a new trip within a period of an existing trip, instead create an activity.");
                              }
                              if (startsAtDateTime.isBefore(tripDb.getEndsAt()) && endsAtDateTime.isAfter(tripDb.getStartsAt())) {
                                        throw new TripDateConflictException("The trip overlaps with an existing trip to: " + tripDb.getDestiny() + " from " + tripDb.getStartsAt());
                              }
                    });
          }

          private void updateTripData(Trip tripDb, UpdateTripRequestPayload payload) {
                    tripDb.setDestiny(payload.destiny());
                    tripDb.setStartsAt(LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME));
                    tripDb.setEndsAt(LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME));
          }

          private Trip buildTripFromPayload(CreateTripRequestPayload payload) {
                    return Trip.builder()
                              .destiny(payload.destiny())
                              .startsAt(LocalDateTime.parse(payload.startsAt(), DATE_TIME_FORMATTER))
                              .endsAt(LocalDateTime.parse(payload.endsAt(), DATE_TIME_FORMATTER))
                              .confirmed(false)
                              .ownerName(payload.ownerName())
                              .ownerEmail(payload.ownerEmail())
                              .build();
          }
}
