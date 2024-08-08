package com.joel.best_travel.infraestructura.abstract_services;

public interface SimpleCrudService<RQ,RS,ID> {
    
    RS create(RQ Request);

    RS read(ID id);

    void delete(ID id);
}
