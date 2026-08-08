package com.sujin.todayjang.market.dto;

import com.sujin.todayjang.market.domain.Market;

// record는 DTO처럼 단순한 객체를 만들 때 보일러플레이트 코드를 줄여줌.
// 생성자, getter 등을 길게 만들지 않아도 자동으로 만들어줌.
public record MarketSearchResponse(

        Long id,
        String name,
        String marketType,
        String province,
        String cityCounty,
        String roadAddress,
        String lotAddress,
        String openingCycle,
        String products,
        Boolean hasParking,
        Boolean hasPublicToilet

) {

    public static MarketSearchResponse from(Market market) {

        return new MarketSearchResponse(
                market.getId(),
                market.getName(),
                market.getMarketType(),
                market.getProvince(),
                market.getCityCounty(),
                market.getRoadAddress(),
                market.getLotAddress(),
                market.getOpeningCycle(),
                market.getProducts(),
                market.getHasParking(),
                market.getHasPublicToilet()
        );
    }
}