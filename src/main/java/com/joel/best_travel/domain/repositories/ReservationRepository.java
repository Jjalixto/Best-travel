package com.joel.best_travel.domain.repositories;

import java.util.UUID;
import org.springframework.data.repository.CrudRepository;

import com.joel.best_travel.domain.entities.ReservationEntity;

public interface ReservationRepository extends CrudRepository<ReservationEntity, UUID>{
}
