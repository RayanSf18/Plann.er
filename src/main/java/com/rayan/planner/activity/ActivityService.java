package com.rayan.planner.activity;

import com.rayan.planner.activity.dto.CreateActivityRequestPayload;
import com.rayan.planner.infra.exceptions.custom.*;
import com.rayan.planner.trip.Trip;
import com.rayan.planner.trip.TripService;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.UUID;

@Service
public class ActivityService {

          private final ActivityRepository activityRepository;
          private final TripService tripService;

          public ActivityService(ActivityRepository activityRepository, @Lazy TripService tripService) {
                    this.activityRepository = activityRepository;
                    this.tripService = tripService;
          }

          public UUID createActivity(UUID tripId, CreateActivityRequestPayload payload) {
                    try {
                              Trip trip = tripService.getTrip(tripId);

                              LocalDateTime activityOccursAt = validateActivityDate(payload.occurs_at());

                              checkIfTripIsConfirmed(trip);

                              checkIfDateOfActivityAlreadyExists(payload, trip);

                              checkIfDateOfActivityIsBetweenTripDate(activityOccursAt, trip.getStartsAt(), trip.getEndsAt());

                              Activity newActivity = new Activity(null, payload.title(), activityOccursAt, trip);

                              activityRepository.save(newActivity);

                              return newActivity.getId();

                    } catch (TripNotFoundException exception) {
                              throw exception;
                    }
          }

          public List<Activity> getAllActivitiesFromTrip(UUID tripId) {
                    try {
                              tripService.getTrip(tripId);
                              return activityRepository.findByTripId(tripId, Sort.by(Sort.Direction.ASC, "occursAt"));
                    } catch (TripNotFoundException exception) {
                              throw exception;
                    }

          }

          private void checkIfTripIsConfirmed(Trip trip) {
                    if (!trip.getConfirmed()) {
                              throw new TripNotConfirmedException("It is not possible to add an activity to a trip that has not yet been confirmed.");
                    }
          }

          private LocalDateTime validateActivityDate(String occursAt) {
                    try {
                              return LocalDateTime.parse(occursAt);
                    } catch (DateTimeParseException e) {
                              throw new InvalidActivityDateException("Invalid date format. Please use yyyy-MM-ddTHH:mm:ss format.");
                    }
          }

          private void checkIfDateOfActivityAlreadyExists(CreateActivityRequestPayload payload, Trip trip) {
                    LocalDateTime occursAt = LocalDateTime.parse(payload.occurs_at());

                    List<Activity> activities = activityRepository.findByTripId(trip.getId());

                    for (Activity activityDb : activities) {
                              if (occursAt.equals(activityDb.getOccursAt())) {
                                        throw new ActivityDateConflictException("The activity: " + activityDb.getTitle() + " will occur on this date " + activityDb.getOccursAt() + ", try a new date or delete the existing one.");
                              }
                    }
          }

          private void checkIfDateOfActivityIsBetweenTripDate(LocalDateTime activityOccursAt, LocalDateTime tripStartsAt, LocalDateTime tripEndsAt) {

                    if (activityOccursAt.isBefore(tripStartsAt) || activityOccursAt.isEqual(tripStartsAt) || activityOccursAt.isEqual(tripEndsAt) || activityOccursAt.isAfter(tripEndsAt)) {
                              throw new InvalidActivityDateException("This date " + activityOccursAt + " is not in the period " + tripStartsAt + " - " + tripEndsAt + " of this trip.");
                    }
          }

}
