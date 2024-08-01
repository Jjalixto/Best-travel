package com.joel.best_travel.infraestructura.abstract_services;

import java.math.BigDecimal;
import java.util.UUID;

import com.joel.best_travel.api.models.request.ReservationRequest;
import com.joel.best_travel.api.models.response.ReservationResponse;

public interface IReservationService extends CrudService<ReservationRequest,ReservationResponse,UUID>{
    
    public BigDecimal findPrice(Long hotelId);
}
