package com.joel.best_travel.infraestructura.helpers;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.domain.entities.jpa.CustomerEntity;
import com.joel.best_travel.domain.entities.jpa.FlyEntity;
import com.joel.best_travel.domain.entities.jpa.HotelEntity;
import com.joel.best_travel.domain.entities.jpa.ReservationEntity;
import com.joel.best_travel.domain.entities.jpa.TicketEntity;
import com.joel.best_travel.domain.repositories.jpa.ReservationRepository;
import com.joel.best_travel.domain.repositories.jpa.TicketRepository;
import com.joel.best_travel.infraestructura.service.ReservationService;
import com.joel.best_travel.infraestructura.service.TicketService;
import com.joel.best_travel.util.BestTravelUtil;

import lombok.AllArgsConstructor;

@Transactional
@Component
@AllArgsConstructor
public class TourHelper {
    
    private final TicketRepository ticketRepository;
    private final ReservationRepository reservationRepository;

    public Set<TicketEntity> createTickets(Set<FlyEntity>fligths,CustomerEntity customer){
        var response = new HashSet<TicketEntity>(fligths.size());
        fligths.forEach(fly -> {
            var ticketToPersist = TicketEntity.builder()
            .id(UUID.randomUUID())
            .fly(fly)
            .customer(customer)
            .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.charger_price_percentage)))
            .purchaseDate(LocalDate.now())
            .departureDate(BestTravelUtil.getRandomSoon())
            .arrivalDate(BestTravelUtil.getRandomLatter())
            .arrivalDate(BestTravelUtil.getRandomLatter())
            .build();
            response.add(this.ticketRepository.save(ticketToPersist));
        });
        return response;
    }
    
    public Set<ReservationEntity> createReservations(HashMap<HotelEntity,Integer>hotels,CustomerEntity customer){
        var response = new HashSet<ReservationEntity>(hotels.size());
        hotels.forEach((hotel,totalDays)->{
            var reservationToPersist = ReservationEntity.builder()
                .id(UUID.randomUUID())
                .hotel(hotel)
                .customer(customer) 
                .totalDays(totalDays)
                .dateTimeReservation(LocalDateTime.now())
                .dateStart(LocalDate.now())
                .dateEnd(LocalDate.now().plusDays(totalDays))
                .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.charges_price_percentage)))
                .build();
            response.add(this.reservationRepository.save(reservationToPersist));
        });
        return response;
    }

    public TicketEntity createTicket(FlyEntity fly, CustomerEntity customer){
        var ticketToPersist = TicketEntity.builder()
            .id(UUID.randomUUID())
            .fly(fly)
            .customer(customer)
            .price(fly.getPrice().add(fly.getPrice().multiply(TicketService.charger_price_percentage)))
            .purchaseDate(LocalDate.now())
            .departureDate(BestTravelUtil.getRandomSoon())
            .arrivalDate(BestTravelUtil.getRandomLatter())
            .arrivalDate(BestTravelUtil.getRandomLatter())
            .build();
            return this.ticketRepository.save(ticketToPersist);
    }

    public ReservationEntity createReservation(HotelEntity hotel, CustomerEntity customer, Integer totalDays){
        var reservationToPersist = ReservationEntity.builder()
            .id(UUID.randomUUID())
            .hotel(hotel)
            .customer(customer)
            .totalDays(totalDays)
            .dateTimeReservation(LocalDateTime.now())
            .dateStart(LocalDate.now())
            .dateEnd(LocalDate.now().plusDays(totalDays))
            .price(hotel.getPrice().add(hotel.getPrice().multiply(ReservationService.charges_price_percentage)))
            .build();
            return this.reservationRepository.save(reservationToPersist);
        }
}
