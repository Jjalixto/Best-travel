package com.joel.best_travel.api.models.request;

import java.io.Serializable;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourFlyRequest implements Serializable{
    
    @Positive
    @NotNull(message = "Id tourFly is mandatory")
    public Long id;
}
