package com.joel.best_travel.infraestructura.abstract_services;

import java.util.UUID;

import com.joel.best_travel.api.models.request.ReservationRequest;
import com.joel.best_travel.domain.entities.ReservationEntity;

public interface IReservationService extends CrudService<ReservationRequest,ReservationEntity,UUID>{
    
}
