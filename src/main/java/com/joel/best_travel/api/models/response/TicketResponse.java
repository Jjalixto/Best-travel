package com.joel.best_travel.api.models.response;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TicketResponse implements Serializable {

    private UUID id;
    private LocalTime departureDate;
    private LocalTime arrivalDate;
    private LocalDate purchaseDate;
    private BigDecimal price;
    private FlyResponse flyResponse;
}
