package com.joel.best_travel.infraestructura.helpers;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.domain.repositories.ReservationRepository;
import com.joel.best_travel.domain.repositories.TicketRepository;

import lombok.AllArgsConstructor;

@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
    
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;
}
