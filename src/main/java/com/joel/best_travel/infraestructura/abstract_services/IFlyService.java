package com.joel.best_travel.infraestructura.abstract_services;

import java.util.Set;

import com.joel.best_travel.api.models.response.FlyResponse;

public interface IFlyService extends CatalogService<FlyResponse>{
    
    Set<FlyResponse> readByOriginDestiny(String origen, String destiny);
}
