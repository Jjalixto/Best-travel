package com.joel.best_travel.api.controllers;

import java.util.Collections;
import java.util.Map;
import java.util.UUID;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.joel.best_travel.api.models.request.TourRequest;
import com.joel.best_travel.api.models.response.TourResponse;
import com.joel.best_travel.infraestructura.abstract_services.ITourService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
@Tag(name = "Tour")
public class TourController {
    
    private final ITourService tourService;

    @Operation(summary = "Save in system in tour based in list of hotels and fligths")
    @PostMapping
    public ResponseEntity<TourResponse>post(@Valid @RequestBody TourRequest request){
        System.out.println(tourService.getClass().getSimpleName());
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @Operation(summary = "Return a tour with of passed")
    @GetMapping("{id}")
    public ResponseEntity<TourResponse>post(@PathVariable Long id){
        return ResponseEntity.ok(this.tourService.read(id));
    }

    @Operation(summary = "Delete a tour with of passed")
    @DeleteMapping("{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Remove a ticket from tour")
    @PatchMapping("{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void>deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId){
        this.tourService.removeTicket(tourId,ticketId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a ticket from tour")
    @PatchMapping("{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String,UUID>>postTicket(@PathVariable Long tourId, @PathVariable Long flyId){
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @Operation(summary = "Remove a reservation from tour")
    @PatchMapping("{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void>deleteReservationId(@PathVariable Long tourId, @PathVariable UUID reservationId){
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Add a reservation from tour")
    @PatchMapping("{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String,UUID>>postTicket(@PathVariable Long tourId, @PathVariable Long hotelId, @RequestParam Integer totalDays){
        var response = Collections.singletonMap("ticketId", this.tourService.addReservation(tourId, hotelId,totalDays));
        return ResponseEntity.ok(response);
    }
}
