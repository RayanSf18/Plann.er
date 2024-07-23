package com.rayan.planner.trip;

import com.rayan.planner.activity.Activity;
import com.rayan.planner.activity.ActivityService;
import com.rayan.planner.activity.dto.ActivityIdResponsePayload;
import com.rayan.planner.activity.dto.ActivityResponsePayload;
import com.rayan.planner.activity.dto.CreateActivityRequestPayload;
import com.rayan.planner.link.Link;
import com.rayan.planner.link.LinkService;
import com.rayan.planner.link.dto.CreateLinkRequestPayload;
import com.rayan.planner.link.dto.LinkIdResponsePayload;
import com.rayan.planner.link.dto.LinkResponsePayload;
import com.rayan.planner.participant.Participant;
import com.rayan.planner.participant.ParticipantService;
import com.rayan.planner.participant.dto.AddParticipantToTripRequestPayload;
import com.rayan.planner.participant.dto.ParticipantIdResponsePayload;
import com.rayan.planner.participant.dto.ParticipantResponsePayload;
import com.rayan.planner.trip.dto.CreateTripRequestPayload;
import com.rayan.planner.trip.dto.TripIdResponsePayload;
import com.rayan.planner.trip.dto.TripResponsePayload;
import com.rayan.planner.trip.dto.UpdateTripRequestPayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/trips", produces = {"application/json"})
@Tag(name = "Trip")
public class TripController {

    private static final Logger logger = LoggerFactory.getLogger(TripController.class);
    private final TripService tripService;
    private final ParticipantService participantService;
    private final ActivityService activityService;
    private final LinkService linkService;

    // Endpoints Trip

    @Operation(summary = "Create a new trip", method = "POST", description = "This endpoint creates a new trip with the provided details.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripIdResponsePayload.class))),
        @ApiResponse(responseCode = "302", description = "Trip already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "409", description = "Date conflict", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripIdResponsePayload> createTrip(@Valid @RequestBody CreateTripRequestPayload payload) {
        UUID tripId = tripService.createTrip(payload);
        URI location = buildResourceLocation(tripId);
        logger.info("Trip created with ID: {}", tripId);
        return ResponseEntity.created(location).body(new TripIdResponsePayload(tripId));
    }

    @Operation(summary = "Search for a specific trip by ID", method = "GET", description = "Fetches the details of a trip using its unique identifier (UUID).")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "User found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponsePayload.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{tripId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripResponsePayload> getTrip(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId) {
        Trip trip = tripService.getTrip(tripId);
        logger.info("Fetched trip with ID: {}", tripId);
        return ResponseEntity.ok(TripResponsePayload.toTripResponse(trip));
    }

    @Operation(summary = "Search all trips", method = "GET", description = "This endpoint searches for all trips within a date range, or if no parameter is passed, returns all trips.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Users found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponsePayload.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<TripResponsePayload>> getAllTrips(
              @Parameter(description = "Start date and time in ISO format (yyyy-MM-ddTHH:mm:ss)", example = "2024-07-25T20:00:00")
              @RequestParam(required = false) String startsAt,
              @Parameter(description = "End date and time in ISO format (yyyy-MM-ddTHH:mm:ss)", example = "2024-07-31T20:00:00")
              @RequestParam(required = false) String endsAt) {
        List<Trip> trips = tripService.getAllTrips(startsAt, endsAt);
        logger.info("Fetched all trips with optional filters: startsAt={}, endsAt={}", startsAt, endsAt);
        return ResponseEntity.ok(trips.stream().map(TripResponsePayload::toTripResponse).toList());
    }

    @Operation(summary = "Confirm the trip", method = "GET", description = "This endpoint serves to confirm that a trip will occur after its creation.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Trip confirmed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponsePayload.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{tripId}/confirm", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripResponsePayload> confirmTrip(@Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000") @PathVariable UUID tripId) {
        Trip trip = tripService.confirmTrip(tripId);
        logger.info("Confirmed trip with ID: {}", tripId);
        return ResponseEntity.ok(TripResponsePayload.toTripResponse(trip));
    }

    @Operation(summary = "Update trip data by id", method = "PUT", description = "This endpoint allows us to update data from an existing trip.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Trip updated", content = @Content(mediaType = "application/json", schema = @Schema(implementation = TripResponsePayload.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PutMapping(value = "/{tripId}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<TripResponsePayload> updateTrip(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId,
              @Valid @RequestBody UpdateTripRequestPayload payload) {
        Trip trip = tripService.updateTrip(tripId, payload);
        logger.info("Updated trip with ID: {}", tripId);
        return ResponseEntity.ok(TripResponsePayload.toTripResponse(trip));
    }

    @Operation(summary = "Delete a trip by id", method = "DELETE", description = "This endpoint allows you to delete an existing trip by id")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "204", description = "Trip deleted"),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId) {
        tripService.deleteTrip(tripId);
        logger.info("Deleted trip with ID: {}", tripId);
        return ResponseEntity.noContent().build();
    }

    // Endpoints Activity
    @Operation(summary = "Create a new activity", method = "POST", description = "This endpoint is used to add a new activity to an existing trip by id.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "201", description = "Activity created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityIdResponsePayload.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "422", description = "Trip not confirmed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "409", description = "Activity date conflicts", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{tripId}/activities", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ActivityIdResponsePayload> createActivity(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId,
              @Valid @RequestBody CreateActivityRequestPayload payload) {
        UUID activityId = activityService.createActivity(tripId, payload);
        URI location = buildResourceLocation(activityId);
        logger.info("Created activity with ID: {} for trip ID: {}", activityId, tripId);
        return ResponseEntity.created(location).body(new ActivityIdResponsePayload(activityId));
    }

    @Operation(summary = "Search all Activities from a trip by ID", method = "GET", description = "This endpoint searches for all activities registered in an existing trip by ID.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Activities found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ActivityResponsePayload.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{tripId}/activities", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ActivityResponsePayload>> getActivitiesForTrip(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId) {
        List<Activity> activities = activityService.getAllActivitiesFromTrip(tripId);
        logger.info("Fetched all activities for trip ID: {}", tripId);
        return ResponseEntity.ok(activities.stream().map(ActivityResponsePayload::toActivityResponse).toList());
    }

    // Endpoints Participant
    @Operation(summary = "Invite a new participant", method = "POST", description = "This endpoint is for inviting a new participant via email to the existing trip by ID.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Invited participant", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParticipantIdResponsePayload.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{tripId}/invite", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<ParticipantIdResponsePayload> inviteParticipant(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId,
              @Valid @RequestBody AddParticipantToTripRequestPayload payload) {
        UUID participantId = tripService.inviteParticipant(tripId, payload);
        logger.info("Invited participant with ID: {} to trip ID: {}", participantId, tripId);
        return ResponseEntity.ok(new ParticipantIdResponsePayload(participantId));
    }

    @Operation(summary = "Search all Participants from a trip by ID", method = "GET", description = "This endpoint searches for all participants registered in an existing trip by ID.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Participants found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParticipantResponsePayload.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{tripId}/participants", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<ParticipantResponsePayload>> getParticipantsForTrip(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId) {
        List<Participant> participants = participantService.getAllParticipantsFromTrip(tripId);
        logger.info("Fetched all participants for trip ID: {}", tripId);
        return ResponseEntity.ok(participants.stream().map(ParticipantResponsePayload::toParticipantResponse).toList());
    }

    // Endpoints Link

    @Operation(summary = "Create a new link", method = "POST", description = "This endpoint is used to add a new link to an existing trip by id.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "201", description = "Link created successfully", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkIdResponsePayload.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "422", description = "Trip not confirmed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "409", description = "Link already exists", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{tripId}/links", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<LinkIdResponsePayload> createLink(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId,
              @Valid @RequestBody CreateLinkRequestPayload payload) {
        UUID linkId = linkService.createLink(tripId, payload);
        URI location = buildResourceLocation(linkId);
        logger.info("Created link with ID: {} for trip ID: {}", linkId, tripId);
        return ResponseEntity.created(location).body(new LinkIdResponsePayload(linkId));
    }

    @Operation(summary = "Search all links from a trip by ID", method = "GET", description = "This endpoint searches for all links registered in an existing trip by ID.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Links found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LinkResponsePayload.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "404", description = "Trip not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @GetMapping(value = "/{tripId}/links", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<LinkResponsePayload>> getLinksForTrip(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID tripId) {
        List<Link> links = linkService.getAllLinksFromTrip(tripId);
        logger.info("Fetched all links for trip ID: {}", tripId);
        return ResponseEntity.ok(links.stream().map(LinkResponsePayload::toLinkResponse).toList());
    }

    private URI buildResourceLocation(UUID resourceId) {
        return ServletUriComponentsBuilder
            .fromCurrentRequest()
            .path("/{id}")
            .buildAndExpand(resourceId)
            .toUri();
    }

}
