package com.joel.best_travel.domain.repositories;

import org.springframework.data.repository.CrudRepository;

import com.joel.best_travel.domain.entities.TourEntity;

public interface TourRepository extends CrudRepository<TourEntity, Long>{
}
