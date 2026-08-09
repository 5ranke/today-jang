package com.sujin.todayjang.market.service;

import com.sujin.todayjang.market.domain.Market;
import com.sujin.todayjang.market.dto.MarketDetailResponse;
import com.sujin.todayjang.market.dto.MarketSearchResponse;
import com.sujin.todayjang.market.dto.NearbyMarketResponse;
import com.sujin.todayjang.market.repository.MarketRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;

@Service
public class MarketSearchService {

    private static final double NEARBY_RADIUS_KM = 20.0;

    private final MarketRepository marketRepository;
    private final MarketDayCalculator marketDayCalculator;
    private final MarketDistanceCalculator marketDistanceCalculator;

    public MarketSearchService(
            MarketRepository marketRepository,
            MarketDayCalculator marketDayCalculator,
            MarketDistanceCalculator marketDistanceCalculator
    ) {
        this.marketRepository = marketRepository;
        this.marketDayCalculator = marketDayCalculator;
        this.marketDistanceCalculator = marketDistanceCalculator;
    }

    public List<MarketSearchResponse> search(
            String province,
            String marketType,
            LocalDate date
    ) {

        List<Market> markets =
                marketRepository.findByProvince(province);

        return markets.stream()
                .filter(market -> matchesMarketType(market, marketType))
                .filter(market ->
                        marketDayCalculator.isOpenOn(
                                market.getOpeningCycle(),
                                date
                        )
                )
                .map(MarketSearchResponse::from)
                .toList();
    }

    private boolean matchesMarketType(Market market, String marketType) {

        return switch (marketType) {
            case "five-day" -> !"상설장".equals(market.getMarketType());
            case "market" -> "상설장".equals(market.getMarketType());
            default -> throw new IllegalArgumentException(
                    "올바르지 않은 시장 유형입니다."
            );
        };
    }

    public MarketDetailResponse getMarket(Long id) {

        Market market = marketRepository.findById(id)
                .orElseThrow(() ->
                        new IllegalArgumentException("존재하지 않는 시장입니다.")
                );

        return MarketDetailResponse.from(market);
    }

    public List<NearbyMarketResponse> searchNearby(
            double latitude,
            double longitude,
            LocalDate date
    ) {

        return marketRepository.findAll()
                .stream()

                // 좌표 정보가 없는 시장은 제외합니다.
                .filter(market ->
                        market.getLatitude() != null
                                && market.getLongitude() != null
                )

                // 선택한 날짜에 열리는 시장만 남깁니다.
                .filter(market ->
                        marketDayCalculator.isOpenOn(
                                market.getOpeningCycle(),
                                date
                        )
                )

                // 사용자와 시장 사이의 거리를 계산합니다.
                .map(market -> {

                    double distanceKm =
                            marketDistanceCalculator.calculate(
                                    latitude,
                                    longitude,
                                    market.getLatitude(),
                                    market.getLongitude()
                            );

                    return NearbyMarketResponse.from(
                            market,
                            distanceKm
                    );
                })

                // 20km 이내 시장만 남깁니다.
                .filter(market ->
                        market.distanceKm() <= NEARBY_RADIUS_KM
                )

                // 가까운 시장부터 정렬합니다.
                .sorted(
                        Comparator.comparingDouble(
                                NearbyMarketResponse::distanceKm
                        )
                )
                .limit(20)
                .toList();
    }
}
