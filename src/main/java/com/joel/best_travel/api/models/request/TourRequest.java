package com.joel.best_travel.api.models.request;

import java.io.Serializable;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class TourRequest implements Serializable{
    
    public Serializable customerId;
    private Set<TourFlyRequest> fligths;
    private Set<TourHotelRequest> hotels;
}
