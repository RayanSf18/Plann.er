package com.rayan.planner.participant;

import com.rayan.planner.participant.dto.ConfirmParticipantRequestPayload;
import com.rayan.planner.participant.dto.ParticipantResponsePayload;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/participants", produces = {"application/json"})
@Tag(name = "Participant")
public class ParticipantController {

    private final ParticipantService participantService;

    @Operation(summary = "Confirm participant", method = "POST", description = "This endpoint serves to confirm a participant after being invited on the trip.")
    @ApiResponses(value = {
              @ApiResponse(responseCode = "200", description = "Participant confirmed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ParticipantResponsePayload.class))),
              @ApiResponse(responseCode = "404", description = "Participant not found", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "422", description = "Trip not confirmed", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "400", description = "Invalid parameters", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class))),
              @ApiResponse(responseCode = "500", description = "Internal Server Error", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProblemDetail.class)))
    })
    @PostMapping(value = "/{participantId}/confirm")
    public ResponseEntity<ParticipantResponsePayload> confirmParticipant(
              @Parameter(description = "Unique identifier of the trip in UUID format", example = "123e4567-e89b-12d3-a456-426614174000")
              @PathVariable UUID participantId,
              @Valid @RequestBody ConfirmParticipantRequestPayload payload) {
        Participant participant = participantService.confirmParticipant(participantId, payload);
        return ResponseEntity.ok().body(ParticipantResponsePayload.toParticipantResponse(participant));
    }
}
