package com.joel.best_travel.infraestructura.abstract_services;

import java.math.BigDecimal;
import java.util.UUID;

import com.joel.best_travel.api.models.request.TicketRequest;
import com.joel.best_travel.api.models.response.TicketResponse;

public interface ITicketService extends CrudService<TicketRequest, TicketResponse, UUID> {
    BigDecimal findPrice(Long flyId);
}
