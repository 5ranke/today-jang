package com.sujin.todayjang.market.dto;

import com.sujin.todayjang.market.domain.Market;

public record MarketDetailResponse(
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
        Boolean hasPublicToilet,
        Boolean hasParking,
        Integer establishedYear,
        String phoneNumber,
        String homepageUrl
) {

    public static MarketDetailResponse from(Market market) {
        return new MarketDetailResponse(
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
                market.getHasPublicToilet(),
                market.getHasParking(),
                market.getEstablishedYear(),
                market.getPhoneNumber(),
                market.getHomepageUrl()
        );
    }
}