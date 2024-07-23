package com.rayan.planner.trip;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;

import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "tb_trips")
public class Trip implements Serializable {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;


    @Column(name = "destiny", nullable = false)
    private String destiny;


    @Column(name = "starts_at", nullable = false)
    private LocalDateTime startsAt;


    @Column(name = "ends_at", nullable = false)
    private LocalDateTime endsAt;


    @Column(name = "confirmed", nullable = false)
    private Boolean confirmed;


    @Column(name = "owner_name", nullable = false)
    private String ownerName;


    @Column(name = "owner_email", nullable = false)
    private String ownerEmail;

//    public Trip(CreateTripRequestPayload payload) {
//        this.destiny = payload.destiny();
//        this.startsAt = LocalDateTime.parse(payload.startsAt(), DateTimeFormatter.ISO_DATE_TIME);
//        this.endsAt = LocalDateTime.parse(payload.endsAt(), DateTimeFormatter.ISO_DATE_TIME);
//        this.confirmed = false;
//        this.ownerName = payload.ownerName();
//        this.ownerEmail = payload.ownerEmail();
//    }
}
