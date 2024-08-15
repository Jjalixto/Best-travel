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

import lombok.AllArgsConstructor;

@RestController
@RequestMapping(path = "tour")
@AllArgsConstructor
public class TourController {
    
    private final ITourService tourService;

    @PostMapping
    public ResponseEntity<TourResponse>post(@RequestBody TourRequest request){
        return ResponseEntity.ok(this.tourService.create(request));
    }

    @GetMapping("{id}")
    public ResponseEntity<TourResponse>post(@PathVariable Long id){
        return ResponseEntity.ok(this.tourService.read(id));
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void>delete(@PathVariable Long id){
        this.tourService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{tourId}/remove_ticket/{ticketId}")
    public ResponseEntity<Void>deleteTicket(@PathVariable Long tourId, @PathVariable UUID ticketId){
        this.tourService.removeTicket(tourId,ticketId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{tourId}/add_ticket/{flyId}")
    public ResponseEntity<Map<String,UUID>>postTicket(@PathVariable Long tourId, @PathVariable Long flyId){
        var response = Collections.singletonMap("ticketId", this.tourService.addTicket(tourId, flyId));
        return ResponseEntity.ok(response);
    }

    @PatchMapping("{tourId}/remove_reservation/{reservationId}")
    public ResponseEntity<Void>deleteReservationId(@PathVariable Long tourId, @PathVariable UUID reservationId){
        this.tourService.removeReservation(tourId, reservationId);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("{tourId}/add_reservation/{hotelId}")
    public ResponseEntity<Map<String,UUID>>postTicket(@PathVariable Long tourId, @PathVariable Long hotelId, @RequestParam Integer totalDays){
        var response = Collections.singletonMap("ticketId", this.tourService.addReservation(tourId, hotelId,totalDays));
        return ResponseEntity.ok(response);
    }
}
