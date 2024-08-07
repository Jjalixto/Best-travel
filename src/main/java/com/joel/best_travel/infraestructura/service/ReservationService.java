package com.joel.best_travel.infraestructura.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.api.models.request.ReservationRequest;
import com.joel.best_travel.api.models.response.HotelResponse;
import com.joel.best_travel.api.models.response.ReservationResponse;
import com.joel.best_travel.domain.entities.ReservationEntity;
import com.joel.best_travel.domain.repositories.CustomerRepository;
import com.joel.best_travel.domain.repositories.HotelRepository;
import com.joel.best_travel.domain.repositories.ReservationRepository;
import com.joel.best_travel.infraestructura.abstract_services.IReservationService;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class ReservationService implements IReservationService{

    private final HotelRepository hotelRepository;
    private final CustomerRepository customerRepository;
    private final ReservationRepository reservationRepository;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow();
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow();
        
        var reservationToPersist = ReservationEntity.builder()
            .id(UUID.randomUUID())
            .hotel(hotel)
            .customer(customer) 
            .totalDays(request.getTotalDays())
            .dateTimeReservation(LocalDateTime.now())
            .dateStart(LocalDate.now())
            .dateEnd(LocalDate.now().plusDays(request.getTotalDays()))
            .price(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)))
            .build();
            
        var reservationPersisted = reservationRepository.save(reservationToPersist);
        
        return this.entityToResponse(reservationPersisted);        
    }

    @Override
    public ReservationResponse read(UUID id) {
        var reservationFromDB = this.reservationRepository.findById(id).orElseThrow();
        return this.entityToResponse(reservationFromDB);
    }

    @Override   
    public ReservationResponse update(ReservationRequest resquest, UUID id) {
        var hotel = this.hotelRepository.findById(resquest.getIdHotel()).orElseThrow();
        var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow();
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(resquest.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(resquest.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)));
        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);
        log.info("Reservation updated with id{}", reservationToUpdate.getId());
        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID id) {
        var reservationToDelete = reservationRepository.findById(id).orElseThrow();
        this.reservationRepository.delete(reservationToDelete);
    }

    private ReservationResponse entityToResponse(ReservationEntity entity){
        var response = new ReservationResponse();
        BeanUtils.copyProperties(entity, response);
        var hotelResponse = new HotelResponse();
        BeanUtils.copyProperties(entity.getHotel(), hotelResponse);
        response.setHotel(hotelResponse);
        return response;
    }

    @Override
    public BigDecimal findPrice(Long hotelId){
        var hotel = hotelRepository.findById(hotelId).orElseThrow();
        return hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage));
    }
    
    private static final BigDecimal charges_price_percentage = BigDecimal.valueOf(0.20);
}
