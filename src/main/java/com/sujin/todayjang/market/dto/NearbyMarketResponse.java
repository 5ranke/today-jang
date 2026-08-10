package com.sujin.todayjang.market.dto;

import com.sujin.todayjang.market.domain.Market;

public record NearbyMarketResponse(
        Long id,
        String name,
        String marketType,
        String province,
        String cityCounty,
        String roadAddress,
        String lotAddress,
        String openingCycle,
        Double latitude,
        Double longitude,
        Integer storeCount,
        String products,
        Boolean hasParking,
        Boolean hasPublicToilet,
        Double distanceKm
) {

    public static NearbyMarketResponse from(
            Market market,
            double distanceKm
    ) {
        return new NearbyMarketResponse(
                market.getId(),
                market.getName(),
                market.getMarketType(),
                market.getProvince(),
                market.getCityCounty(),
                market.getRoadAddress(),
                market.getLotAddress(),
                market.getOpeningCycle(),
                market.getLatitude(),
                market.getLongitude(),
                market.getStoreCount(),
                market.getProducts(),
                market.getHasParking(),
                market.getHasPublicToilet(),
                distanceKm
        );
    }
}
