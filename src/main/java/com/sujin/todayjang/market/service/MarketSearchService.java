package com.sujin.todayjang.market.service;

import com.sujin.todayjang.market.domain.Market;
import com.sujin.todayjang.market.dto.MarketRangeSearchResponse;
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

    public List<MarketRangeSearchResponse> searchRange(
            String province,
            String cityCounty,
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "시작 날짜는 종료 날짜보다 늦을 수 없습니다."
            );
        }

        List<Market> markets =
                marketRepository.findByProvinceAndCityCounty(
                        province,
                        cityCounty
                );

        return markets.stream()
                .map(market -> {

                    List<LocalDate> openDates =
                            startDate
                                    .datesUntil(endDate.plusDays(1))
                                    .filter(date ->
                                            marketDayCalculator.isOpenOn(
                                                    market.getOpeningCycle(),
                                                    date
                                            )
                                    )
                                    .toList();

                    if (openDates.isEmpty()) {
                        return null;
                    }

                    return MarketRangeSearchResponse.from(
                            market,
                            openDates
                    );
                })
                .filter(response -> response != null)
                .toList();
    }
}