package com.joel.best_travel.infraestructura.abstract_services;

import java.math.BigDecimal;
import java.util.Set;

import org.springframework.data.domain.Page;

import com.joel.best_travel.util.SortType;

public interface CatalogService<R> {

    Page<R> realAll(Integer page, Integer size, SortType sortType);

    Set<R> readLessPrice(BigDecimal price);

    Set<R> readBetweenPrices(BigDecimal min,BigDecimal max);

    String FIELD_BY_SORT = "price";
}