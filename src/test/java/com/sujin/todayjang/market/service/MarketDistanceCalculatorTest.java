package com.sujin.todayjang.market.service;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MarketDistanceCalculatorTest {

    private final MarketDistanceCalculator calculator =
            new MarketDistanceCalculator();

    @Test
    void 같은_위치의_거리는_0이다() {

        double distance = calculator.calculate(
                37.5665,
                126.9780,
                37.5665,
                126.9780
        );

        assertThat(distance)
                .isCloseTo(
                        0.0,
                        org.assertj.core.data.Offset.offset(0.001)
                );
    }

    @Test
    void 서로_다른_위치의_거리를_계산할_수_있다() {

        // 서울시청 근처
        double latitude1 = 37.5665;
        double longitude1 = 126.9780;

        // 서울역 근처
        double latitude2 = 37.5547;
        double longitude2 = 126.9706;

        double distance = calculator.calculate(
                latitude1,
                longitude1,
                latitude2,
                longitude2
        );

        assertThat(distance)
                .isBetween(1.0, 2.0);
    }

    @Test
    void 출발지와_도착지를_바꿔도_거리는_같다() {

        double distance1 = calculator.calculate(
                37.5665,
                126.9780,
                37.5547,
                126.9706
        );

        double distance2 = calculator.calculate(
                37.5547,
                126.9706,
                37.5665,
                126.9780
        );

        assertThat(distance1)
                .isCloseTo(
                        distance2,
                        org.assertj.core.data.Offset.offset(0.001)
                );
    }
}