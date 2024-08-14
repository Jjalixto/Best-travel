package com.joel.best_travel.infraestructura.abstract_services;

import java.util.HashMap;
import java.util.HashSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.api.models.request.TourRequest;
import com.joel.best_travel.api.models.response.TourResponse;
import com.joel.best_travel.domain.entities.FlyEntity;
import com.joel.best_travel.domain.entities.HotelEntity;
import com.joel.best_travel.domain.entities.ReservationEntity;
import com.joel.best_travel.domain.entities.TicketEntity;
import com.joel.best_travel.domain.entities.TourEntity;
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
    public TourResponse create(TourRequest request) {
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow();
        var fligths = new HashSet<FlyEntity>();
        request.getFligths().forEach(fly -> fligths.add(this.flyRepository.findById(fly.getId()).orElseThrow()));
        var hotels = new HashMap<HotelEntity,Integer>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(
            hotel.getId()).orElseThrow(),hotel.getTotalDays()));
        var tourToSave = TourEntity.builder()
            .tickets(this.tourHelper.createTickets(fligths, customer))
            .reservations(this.tourHelper.createReservations(hotels, customer))
            .customer(customer)
            .build();
        var tourSaved = this.tourRepository.save(tourToSave);
        return TourResponse.builder()
            .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
            .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
            .id(tourSaved.getId())
            .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = this.tourRepository.findById(id).orElseThrow();
        return TourResponse.builder()
            .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
            .ticketIds(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
            .id(tourFromDb.getId())
            .build();
    }

    @Override
    public void delete(Long id) {
        var tourDelete = this.tourRepository.findById(id).orElseThrow();
        this.tourRepository.delete(tourDelete);
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
