package com.sujin.todayjang.market.dto;

import com.sujin.todayjang.market.domain.Market;

import java.time.LocalDate;
import java.util.List;

public record MarketRangeSearchResponse(
        Long id,
        String name,
        String marketType,
        String province,
        String cityCounty,
        String roadAddress,
        String openingCycle,
        String products,
        Boolean hasParking,
        Boolean hasPublicToilet,
        List<LocalDate> openDates
) {

    public static MarketRangeSearchResponse from(
            Market market,
            List<LocalDate> openDates
    ) {
        return new MarketRangeSearchResponse(
                market.getId(),
                market.getName(),
                market.getMarketType(),
                market.getProvince(),
                market.getCityCounty(),
                market.getRoadAddress(),
                market.getOpeningCycle(),
                market.getProducts(),
                market.getHasParking(),
                market.getHasPublicToilet(),
                openDates
        );
    }
}