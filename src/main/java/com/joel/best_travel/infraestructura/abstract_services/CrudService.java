package com.joel.best_travel.infraestructura.abstract_services;

public interface CrudService<RQ, RS, ID> {

    RS create(RQ request);

    RS read(ID id);

    RS update(RQ resquest, ID id);

    void delete(ID id);
}
