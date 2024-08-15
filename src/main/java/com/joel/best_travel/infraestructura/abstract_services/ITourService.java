package com.joel.best_travel.infraestructura.abstract_services;

import java.util.UUID;

import com.joel.best_travel.api.models.request.TourRequest;
import com.joel.best_travel.api.models.response.TourResponse;

public interface ITourService extends SimpleCrudService<TourRequest,TourResponse,Long>{
    
    void removeTicket(Long tourId,UUID ticketId);

    UUID addTicket(Long flyId,Long tourId);

    void removeReservation(Long tourId,UUID reservationId);
    
    UUID addReservation(Long reservationId,Long tourId, Integer totalDays);
}
