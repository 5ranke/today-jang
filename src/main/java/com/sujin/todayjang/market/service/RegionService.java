package com.sujin.todayjang.market.service;

import com.sujin.todayjang.market.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RegionService {

    private final MarketRepository marketRepository;

    public RegionService(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    public List<String> getProvinces() {
        return marketRepository.findDistinctProvinces();
    }

    public List<String> getCityCounties(String province) {
        return marketRepository.findDistinctCityCountiesByProvince(province);
    }
}