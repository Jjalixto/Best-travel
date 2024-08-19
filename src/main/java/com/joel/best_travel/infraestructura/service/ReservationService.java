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
import com.joel.best_travel.infraestructura.helpers.BlackListHelper;
import com.joel.best_travel.infraestructura.helpers.CustomerHelper;
import com.joel.best_travel.util.enums.Tables;
import com.joel.best_travel.util.exceptions.IdNotFoundException;

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
    private final CustomerHelper customerHelper;
    private BlackListHelper blackListHelper;

    @Override
    public ReservationResponse create(ReservationRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var hotel = this.hotelRepository.findById(request.getIdHotel()).orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        var customer = this.customerRepository.findById(request.getIdClient()).orElseThrow(()-> new IdNotFoundException(Tables.customer.name()));
        
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
        this.customerHelper.increase(customer.getDni(), ReservationService.class);
        return this.entityToResponse(reservationPersisted);        
    }

    @Override
    public ReservationResponse read(UUID id) {
        var reservationFromDB = this.reservationRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        return this.entityToResponse(reservationFromDB);
    }

    @Override   
    public ReservationResponse update(ReservationRequest resquest, UUID id) {
        var hotel = this.hotelRepository.findById(resquest.getIdHotel()).orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        var reservationToUpdate = this.reservationRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.reservation.name()));
        reservationToUpdate.setHotel(hotel);
        reservationToUpdate.setTotalDays(resquest.getTotalDays());
        reservationToUpdate.setDateTimeReservation(LocalDateTime.now());
        reservationToUpdate.setDateStart(LocalDate.now());
        reservationToUpdate.setDateEnd(LocalDate.now().plusDays(resquest.getTotalDays()));
        reservationToUpdate.setPrice(hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage)));
        var reservationUpdated = this.reservationRepository.save(reservationToUpdate);
        log.info("Reservation updated with id =", reservationToUpdate.getId());
        return this.entityToResponse(reservationUpdated);
    }

    @Override
    public void delete(UUID id) {
        var reservationToDelete = reservationRepository.findById(id).orElseThrow(()-> new IdNotFoundException(Tables.reservation.name()));
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
        var hotel = hotelRepository.findById(hotelId).orElseThrow(()-> new IdNotFoundException(Tables.hotel.name()));
        return hotel.getPrice().add(hotel.getPrice().multiply(charges_price_percentage));
    }
    
    public static final BigDecimal charges_price_percentage = BigDecimal.valueOf(0.20);
}
