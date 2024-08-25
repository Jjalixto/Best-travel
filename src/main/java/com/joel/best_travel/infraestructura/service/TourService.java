package com.joel.best_travel.infraestructura.service;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
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
import com.joel.best_travel.infraestructura.abstract_services.ITourService;
import com.joel.best_travel.infraestructura.helpers.BlackListHelper;
import com.joel.best_travel.infraestructura.helpers.CustomerHelper;
import com.joel.best_travel.infraestructura.helpers.EmailHelper;
import com.joel.best_travel.infraestructura.helpers.TourHelper;
import com.joel.best_travel.util.enums.Tables;
import com.joel.best_travel.util.exceptions.IdNotFoundException;

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
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public TourResponse create(TourRequest request) {
        blackListHelper.isInBlackListCustomer(request.getCustomerId());
        var customer = customerRepository.findById(request.getCustomerId()).orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));
        var fligths = new HashSet<FlyEntity>();
        request.getFlights().forEach(fly -> fligths.add(this.flyRepository.findById(fly.getId()).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()))));
        var hotels = new HashMap<HotelEntity,Integer>();
        request.getHotels().forEach(hotel -> hotels.put(this.hotelRepository.findById(
            hotel.getId()).orElseThrow(),hotel.getTotalDays()));
        var tourToSave = TourEntity.builder()
            .tickets(this.tourHelper.createTickets(fligths, customer))
            .reservations(this.tourHelper.createReservations(hotels, customer))
            .customer(customer)
            .build();
        var tourSaved = this.tourRepository.save(tourToSave);
        this.customerHelper.increase(customer.getDni(), TourService.class);
        if(Objects.nonNull(request.getEmail()))this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.tour.name());
        return TourResponse.builder()
            .reservationIds(tourSaved.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
            .ticketIds(tourSaved.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
            .id(tourSaved.getId())
            .build();
    }

    @Override
    public TourResponse read(Long id) {
        var tourFromDb = this.tourRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        return TourResponse.builder()
            .reservationIds(tourFromDb.getReservations().stream().map(ReservationEntity::getId).collect(Collectors.toSet()))
            .ticketIds(tourFromDb.getTickets().stream().map(TicketEntity::getId).collect(Collectors.toSet()))
            .id(tourFromDb.getId())
            .build();
    }

    @Override
    public void delete(Long id) {
        var tourDelete = this.tourRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        this.tourRepository.delete(tourDelete);
    }

    @Override
    public void removeTicket(Long tourId,UUID ticketId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeTicket(ticketId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addTicket(Long flyId, Long tourId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        var fly = this.flyRepository.findById(flyId).orElseThrow(()-> new IdNotFoundException(Tables.fly.name()));
        var ticket = this.tourHelper.createTicket(fly, tourUpdate.getCustomer());
        tourUpdate.addTicket(ticket);
        this.tourRepository.save(tourUpdate);
        return ticket.getId();
    }

    @Override
    public void removeReservation(Long tourId, UUID reservationId) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        tourUpdate.removeReservation(reservationId);
        this.tourRepository.save(tourUpdate);
    }

    @Override
    public UUID addReservation(Long tourId, Long hotelId,Integer totalDays) {
        var tourUpdate = this.tourRepository.findById(tourId).orElseThrow(()-> new IdNotFoundException(Tables.tour.name()));
        var hotel = this.hotelRepository.findById(hotelId).orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        var reservation = this.tourHelper.createReservation(hotel, tourUpdate.getCustomer(),totalDays);
        tourUpdate.addReservation(reservation);
        this.tourRepository.save(tourUpdate);
        return reservation.getId();
    }
    
}
