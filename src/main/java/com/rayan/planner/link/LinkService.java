package com.rayan.planner.link;

import com.rayan.planner.infra.exceptions.custom.LinkAlreadyExistsException;
import com.rayan.planner.infra.exceptions.custom.TripNotConfirmedException;
import com.rayan.planner.infra.exceptions.custom.TripNotFoundException;
import com.rayan.planner.link.dto.CreateLinkRequestPayload;
import com.rayan.planner.trip.Trip;
import com.rayan.planner.trip.TripService;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class LinkService {

    private final LinkRepository linkRepository;
    private final TripService tripService;

    public LinkService(LinkRepository linkRepository, @Lazy TripService tripService) {
        this.linkRepository = linkRepository;
        this.tripService = tripService;
    }

    public UUID createLink(UUID tripId, CreateLinkRequestPayload payload) {
        try {
            Trip trip = tripService.getTrip(tripId);

            checkIfTripIsConfirmed(trip);

            checkIfLinkAlreadyExistsAtTrip(payload, trip);

            Link newLink = new Link(null, payload.title(), payload.url(), trip);

            linkRepository.save(newLink);

            return newLink.getId();

        } catch (TripNotFoundException exception) {
            throw exception;
        }
    }

    public List<Link> getAllLinksFromTrip(UUID tripId) {
        try {
            tripService.getTrip(tripId);
            return linkRepository.findByTripId(tripId);
        } catch (TripNotFoundException exception) {
            throw exception;
        }
    }

    private void checkIfLinkAlreadyExistsAtTrip(CreateLinkRequestPayload payload, Trip trip) {
        List<Link> links = linkRepository.findByTripId(trip.getId());
        for (Link linkDb : links) {
            if (payload.url().equals(linkDb.getUrl())) {
                throw new LinkAlreadyExistsException("This url link is already registered on this trip with the name: " + linkDb.getTitle());
            }
        }
    }

    private void checkIfTripIsConfirmed(Trip trip) {
        if (!trip.getConfirmed()) {
            throw new TripNotConfirmedException("It is not possible to register the link on a trip that has not yet been confirmed.");
        }
    }

}
