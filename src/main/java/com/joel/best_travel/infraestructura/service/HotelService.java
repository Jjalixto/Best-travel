package com.joel.best_travel.infraestructura.service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.joel.best_travel.api.models.response.HotelResponse;
import com.joel.best_travel.domain.entities.jpa.HotelEntity;
import com.joel.best_travel.domain.repositories.jpa.HotelRepository;
import com.joel.best_travel.infraestructura.abstract_services.IHotelService;
import com.joel.best_travel.util.SortType;
import com.joel.best_travel.util.constants.CacheConstants;

import lombok.AllArgsConstructor;

@Transactional(readOnly = true)
@Service
@AllArgsConstructor
public class HotelService implements IHotelService{

    private final HotelRepository hotelRepository;

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Page<HotelResponse> realAll(Integer page, Integer size, SortType sortType) {
        try {
            Thread.sleep(7000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        PageRequest pageRequest = null;
        switch(sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.hotelRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readLessPrice(BigDecimal price) {
        try {
            Thread.sleep(7000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.hotelRepository.findByPriceLessThan(price)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        try {
            Thread.sleep(7000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.hotelRepository.findByPriceBetween(min,max)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.HOTEL_CACHE_NAME)
    public Set<HotelResponse> readByRating(Integer rating) {
        try {
            Thread.sleep(7000);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this.hotelRepository.findByRatingGreaterThan(rating)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toSet());
    }
    
    private HotelResponse entityToResponse(HotelEntity entity){
        HotelResponse response = new HotelResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
