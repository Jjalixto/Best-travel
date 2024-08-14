package com.joel.best_travel.infraestructura.abstract_services;

import java.util.UUID;

import com.joel.best_travel.api.models.request.TourRequest;
import com.joel.best_travel.api.models.response.TourResponse;

public interface ITourService extends SimpleCrudService<TourRequest,TourResponse,Long>{
    
    void remuveTicket(UUID ticketId,Long tourId);

    UUID addTicket(Long flyId,Long tourId);

    void remuveReservation(UUID reservationId,Long tourId);
    
    UUID addReservation(Long reservationId,Long tourId);
}
