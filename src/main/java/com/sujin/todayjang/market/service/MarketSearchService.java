package com.sujin.todayjang.market.service;

import com.sujin.todayjang.market.domain.Market;
import com.sujin.todayjang.market.dto.MarketSearchResponse;
import com.sujin.todayjang.market.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class MarketSearchService {

    private final MarketRepository marketRepository;
    private final MarketDayCalculator marketDayCalculator;

    public MarketSearchService(
            MarketRepository marketRepository,
            MarketDayCalculator marketDayCalculator
    ) {
        this.marketRepository = marketRepository;
        this.marketDayCalculator = marketDayCalculator;
    }

    public List<MarketSearchResponse> search(
            String province,
            String cityCounty,
            LocalDate date
    ) {

        List<Market> markets =
                marketRepository.findByProvinceAndCityCounty(
                        province,
                        cityCounty
                );

        return markets.stream()
                .filter(market ->
                        marketDayCalculator.isOpenOn(
                                market.getOpeningCycle(),
                                date
                        )
                )
                .map(MarketSearchResponse::from)
                .toList();
    }
}