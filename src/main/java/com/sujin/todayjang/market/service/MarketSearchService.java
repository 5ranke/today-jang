package com.sujin.todayjang.market.service;

import com.sujin.todayjang.market.domain.Market;
import com.sujin.todayjang.market.dto.MarketDetailResponse;
import com.sujin.todayjang.market.dto.MarketRangeSearchResponse;
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
            LocalDate date
    ) {

        List<Market> markets =
                marketRepository.findByProvince(province);

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
            LocalDate startDate,
            LocalDate endDate
    ) {

        if (startDate.isAfter(endDate)) {
            throw new IllegalArgumentException(
                    "시작 날짜는 종료 날짜보다 늦을 수 없습니다."
            );
        }

        List<Market> markets =
                marketRepository.findByProvince(province);

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
