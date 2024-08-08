package com.joel.best_travel.infraestructura.abstract_services;

import java.util.Set;

import com.joel.best_travel.api.models.response.HotelResponse;

public interface IHotelService extends CatalogService<HotelResponse>{
    
    Set<HotelResponse> readByRating(Integer rating);
}
