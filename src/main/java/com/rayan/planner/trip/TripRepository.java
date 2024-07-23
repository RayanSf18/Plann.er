package com.rayan.planner.trip;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

public interface TripRepository extends JpaRepository<Trip, UUID> {

    @Query("SELECT t FROM Trip t WHERE " +
              "(t.startsAt <= :endsAt AND t.endsAt >= :startsAt)")
    Optional<Trip> findConflictingTrip(@Param("startsAt") LocalDateTime startsAt, @Param("endsAt") LocalDateTime endsAt);

    boolean existsByDestinyAndStartsAt(String destiny, LocalDateTime startsAt);
}
