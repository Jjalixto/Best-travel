package com.joel.best_travel;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.joel.best_travel.domain.entities.ReservationEntity;
import com.joel.best_travel.domain.entities.TicketEntity;
import com.joel.best_travel.domain.entities.TourEntity;
import com.joel.best_travel.domain.repositories.CustomerRepository;
import com.joel.best_travel.domain.repositories.FlyRepository;
import com.joel.best_travel.domain.repositories.HotelRepository;
import com.joel.best_travel.domain.repositories.ReservationRepository;
import com.joel.best_travel.domain.repositories.TicketRepository;
import com.joel.best_travel.domain.repositories.TourRepository;

import lombok.extern.slf4j.Slf4j;

@SpringBootApplication
@Slf4j
public class BestTravelApplication implements CommandLineRunner {

	private final HotelRepository hotelRepository;

	private final FlyRepository flyRepository;

	private final TicketRepository ticketRepository;

	private final ReservationRepository reservationRepository;

	private final TourRepository tourRepository;

	private final CustomerRepository customerRepository;

	public BestTravelApplication(HotelRepository hotelRepository,
			FlyRepository flyRepository,
			TicketRepository ticketRepository,
			ReservationRepository reservationRepository,
			TourRepository tourRepository,
			CustomerRepository customerRepository) {
		this.hotelRepository = hotelRepository;
		this.flyRepository = flyRepository;
		this.ticketRepository = ticketRepository;
		this.reservationRepository = reservationRepository;
		this.tourRepository = tourRepository;
		this.customerRepository = customerRepository;
	}

	public static void main(String[] args) {
		SpringApplication.run(BestTravelApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

		var customer = customerRepository.findById("GOTW771012HMRGR087").orElseThrow();
		log.info("Client name " + customer.getFullName());

		var fly = flyRepository.findById(11L).orElseThrow();
		log.info("fly " + fly.getOriginName() + "-" + fly.getDestinyName());

		var hotel = hotelRepository.findById(3L).orElseThrow();
		log.info("Hotel " + hotel.getName());

		var tour = TourEntity.builder()
				.customer(customer)
				.build();

		var ticket = TicketEntity.builder()
				.id(UUID.randomUUID())
				.price(fly.getPrice().multiply(BigDecimal.TEN))
				.arrivalDate(LocalDate.now())
				.departureDate(LocalDate.now())
				.purchaseDate(LocalDate.now())
				.customer(customer)
				.tour(tour)
				.fly(fly)
				.build();

		var reservation = ReservationEntity.builder()
				.id(UUID.randomUUID())
				.dateTimeReservation(LocalDateTime.now())
				.dateEnd(LocalDate.now().plusDays(2))
				.dateStart(LocalDate.now().plusDays(1))
				.hotel(hotel)
				.customer(customer)
				.tour(tour)
				.totalDays(1)
				.price(hotel.getPrice().multiply(BigDecimal.TEN))
				.build();

		System.out.println("---SAVING---");

		var tourSaved = this.tourRepository.save(tour);
		Thread.sleep(8000);
		this.tourRepository.deleteById(tourSaved.getId());
	}
}
