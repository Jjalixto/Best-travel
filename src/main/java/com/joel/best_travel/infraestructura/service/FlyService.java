package com.joel.best_travel.infraestructura.service;

import java.math.BigDecimal;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import com.joel.best_travel.api.models.response.FlyResponse;
import com.joel.best_travel.domain.entities.FlyEntity;
import com.joel.best_travel.domain.repositories.FlyRepository;
import com.joel.best_travel.infraestructura.abstract_services.IFlyService;
import com.joel.best_travel.util.SortType;
import com.joel.best_travel.util.constants.CacheConstants;

@Transactional(readOnly = true)
@Service
public class FlyService implements IFlyService{

    private final FlyRepository flyRepository;
    private final WebClient webClient;

    public FlyService(FlyRepository flyRepository,@Qualifier(value = "base") WebClient webClient){
        this.flyRepository = flyRepository;
        this.webClient = webClient;
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Page<FlyResponse> realAll(Integer page, Integer size, SortType sortType) {
        PageRequest pageRequest = null;
        switch(sortType){
            case NONE -> pageRequest = PageRequest.of(page, size);
            case LOWER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).ascending());
            case UPPER -> pageRequest = PageRequest.of(page, size, Sort.by(FIELD_BY_SORT).descending());
        }
        return this.flyRepository.findAll(pageRequest).map(this::entityToResponse);
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readLessPrice(BigDecimal price) {
        return this.flyRepository.selectLessPrice(price)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readBetweenPrices(BigDecimal min, BigDecimal max) {
        return this.flyRepository.selectBetweenPrice(min,max)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toSet());
    }

    @Override
    @Cacheable(value = CacheConstants.FLY_CACHE_NAME)
    public Set<FlyResponse> readByOriginDestiny(String origen, String destiny) {
        return this.flyRepository.selectOriginDestiny(origen,destiny)
            .stream()
            .map(this::entityToResponse)
            .collect(Collectors.toSet());
    }
    
    private FlyResponse entityToResponse(FlyEntity entity){
        FlyResponse response = new FlyResponse();
        BeanUtils.copyProperties(entity, response);
        return response;
    }
}
