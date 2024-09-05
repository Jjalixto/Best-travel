package com.joel.best_travel.infraestructura.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.api.models.request.TicketRequest;
import com.joel.best_travel.api.models.response.FlyResponse;
import com.joel.best_travel.api.models.response.TicketResponse;
import com.joel.best_travel.domain.entities.jpa.TicketEntity;
import com.joel.best_travel.domain.repositories.jpa.CustomerRepository;
import com.joel.best_travel.domain.repositories.jpa.FlyRepository;
import com.joel.best_travel.domain.repositories.jpa.TicketRepository;
import com.joel.best_travel.infraestructura.abstract_services.ITicketService;
import com.joel.best_travel.infraestructura.helpers.BlackListHelper;
import com.joel.best_travel.infraestructura.helpers.CustomerHelper;
import com.joel.best_travel.infraestructura.helpers.EmailHelper;
import com.joel.best_travel.util.BestTravelUtil;
import com.joel.best_travel.util.enums.Tables;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Transactional
@Service
@Slf4j
@AllArgsConstructor
public class TicketService implements ITicketService {

    private final FlyRepository flyRepository;
    private final CustomerRepository customerRepository;
    private final TicketRepository ticketRepository;
    private final CustomerHelper customerHelper;
    private final BlackListHelper blackListHelper;
    private final EmailHelper emailHelper;

    @Override
    public TicketResponse create(TicketRequest request) {
        blackListHelper.isInBlackListCustomer(request.getIdClient());
        var fly = flyRepository.findById(request.getIdFly()).orElseThrow();
        var customer = customerRepository.findById(request.getIdClient()).orElseThrow();
        var ticketToPersist = TicketEntity.builder()
                .id(UUID.randomUUID())
                .fly(fly)
                .customer(customer)
                .price(fly.getPrice().multiply(charger_price_percentage))
                .purchaseDate(LocalDate.now())
                .departureDate(BestTravelUtil.getRandomSoon())
                .arrivalDate(BestTravelUtil.getRandomLatter())
                .build();
        var ticketPersisted = this.ticketRepository.save(ticketToPersist);
        customerHelper.increase(customer.getDni(), TicketService.class);
        log.info("Ticked saved with id: {}" + ticketPersisted.getId());
        if(Objects.nonNull(request.getEmail()))this.emailHelper.sendMail(request.getEmail(), customer.getFullName(), Tables.ticket.name());
        return this.entityToResponse(ticketPersisted);
    }

    @Override
    public TicketResponse read(UUID id) {
        var ticketFromDB = this.ticketRepository.findById(id).orElseThrow();
        return this.entityToResponse(ticketFromDB);
    }

    @Override
    public TicketResponse update(TicketRequest resquest, UUID id) {
        var ticketToUpdate = ticketRepository.findById(id).orElseThrow();
        var fly = flyRepository.findById(resquest.getIdFly()).orElseThrow();

        ticketToUpdate.setFly(fly);
        ticketToUpdate.setPrice(fly.getPrice().multiply(charger_price_percentage));
        ticketToUpdate.setDepartureDate(BestTravelUtil.getRandomSoon());
        ticketToUpdate.setArrivalDate(BestTravelUtil.getRandomLatter());

        var ticketUpdate = this.ticketRepository.save(ticketToUpdate);

        log.info("Ticket updated  with id {}"+ ticketUpdate.getId());

        return this.entityToResponse(ticketToUpdate);
    }

    @Override
    public void delete(UUID id) {
        var ticketToDelete = ticketRepository.findById(id).orElseThrow();
        this.ticketRepository.delete(ticketToDelete);
    }

    @Override
    public BigDecimal findPrice(Long flyId) {
        var fly = this.flyRepository.findById(flyId).orElseThrow();
        return fly.getPrice().multiply(charger_price_percentage);
    }

    private TicketResponse entityToResponse(TicketEntity entity) {
        var response = new TicketResponse();
        BeanUtils.copyProperties(entity, response);
        var flyResponse = new FlyResponse();
        BeanUtils.copyProperties(entity.getFly(), flyResponse);
        response.setFly(flyResponse);
        return response;
    }

    public static final BigDecimal charger_price_percentage = BigDecimal.valueOf(0.25);
}
