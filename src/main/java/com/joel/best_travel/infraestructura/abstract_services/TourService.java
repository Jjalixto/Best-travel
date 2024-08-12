package com.joel.best_travel.infraestructura.abstract_services;

import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.api.models.request.TourRequest;
import com.joel.best_travel.api.models.response.TourResponse;
import com.joel.best_travel.domain.repositories.CustomerRepository;
import com.joel.best_travel.domain.repositories.FlyRepository;
import com.joel.best_travel.domain.repositories.HotelRepository;
import com.joel.best_travel.domain.repositories.TourRepository;
import com.joel.best_travel.infraestructura.helpers.TourHelper;

import lombok.AllArgsConstructor;

@Transactional
@Service
@AllArgsConstructor
public class TourService implements ITourService{

    private final TourRepository tourRepository;
    private final FlyRepository flyRepository;
    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final TourHelper tourHelper;

    @Override
    public TourResponse create(TourRequest Request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'create'");
    }

    @Override
    public TourResponse read(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'read'");
    }

    @Override
    public void delete(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'delete'");
    }

    @Override
    public void remuveTicket(UUID ticketId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remuveTicket'");
    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addTicket'");
    }

    @Override
    public void remuveReservation(UUID reservationId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'remuveReservation'");
    }

    @Override
    public UUID addReservation(Long reservationId, Long tourId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addReservation'");
    }
    
}
