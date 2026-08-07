package com.sujin.todayjang.market.service;

import org.junit.jupiter.api.Test; // 테스트 메서드를 표시합니다.

import java.time.LocalDate; // 테스트 날짜를 만들기 위해 사용합니다.

import static org.assertj.core.api.Assertions.assertThat; // 실제 결과와 기대 결과를 비교합니다.

class MarketDayCalculatorTest {

    // 테스트할 계산기를 생성합니다.
    private final MarketDayCalculator calculator =
            new MarketDayCalculator();

    @Test
    void 매일_운영하는_시장은_항상_열린다() {

        // 테스트할 날짜를 만듭니다.
        LocalDate date = LocalDate.of(2026, 8, 8);

        // "매일" 시장이 해당 날짜에 열리는지 계산합니다.
        boolean result =
                calculator.isOpenOn("매일", date);

        // 결과가 true인지 확인합니다.
        assertThat(result).isTrue();
    }

    @Test
    void 이일_칠일장은_끝자리가_2또는7인_날에_열린다() {

        // 8월 7일은 장날입니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+7일",
                        LocalDate.of(2026, 8, 7)
                )
        ).isTrue();

        // 8월 12일도 장날입니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+7일",
                        LocalDate.of(2026, 8, 12)
                )
        ).isTrue();

        // 8월 22일도 장날입니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+7일",
                        LocalDate.of(2026, 8, 22)
                )
        ).isTrue();

        // 8월 27일도 장날입니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+7일",
                        LocalDate.of(2026, 8, 27)
                )
        ).isTrue();
    }

    @Test
    void 이일_칠일장은_8일에는_열리지_않는다() {

        // 8월 8일은 2일장도 7일장도 아닙니다.
        boolean result =
                calculator.isOpenOn(
                        "2일+7일",
                        LocalDate.of(2026, 8, 8)
                );

        // 열리지 않아야 하므로 false를 기대합니다.
        assertThat(result).isFalse();
    }

    @Test
    void 오일_십일장은_5일과_10일_주기로_열린다() {

        // 5일
        assertThat(
                calculator.isOpenOn(
                        "5일+10일",
                        LocalDate.of(2026, 8, 5)
                )
        ).isTrue();

        // 10일
        assertThat(
                calculator.isOpenOn(
                        "5일+10일",
                        LocalDate.of(2026, 8, 10)
                )
        ).isTrue();

        // 15일
        assertThat(
                calculator.isOpenOn(
                        "5일+10일",
                        LocalDate.of(2026, 8, 15)
                )
        ).isTrue();

        // 20일
        assertThat(
                calculator.isOpenOn(
                        "5일+10일",
                        LocalDate.of(2026, 8, 20)
                )
        ).isTrue();

        // 25일
        assertThat(
                calculator.isOpenOn(
                        "5일+10일",
                        LocalDate.of(2026, 8, 25)
                )
        ).isTrue();

        // 30일
        assertThat(
                calculator.isOpenOn(
                        "5일+10일",
                        LocalDate.of(2026, 8, 30)
                )
        ).isTrue();
    }

    @Test
    void 여러개의_장날도_계산할_수_있다() {

        // 14일 → 끝자리가 4이므로 장날입니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+4일+7일+9일",
                        LocalDate.of(2026, 8, 14)
                )
        ).isTrue();

        // 17일 → 끝자리가 7이므로 장날입니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+4일+7일+9일",
                        LocalDate.of(2026, 8, 17)
                )
        ).isTrue();

        // 15일 → 어떤 규칙에도 해당하지 않습니다.
        assertThat(
                calculator.isOpenOn(
                        "2일+4일+7일+9일",
                        LocalDate.of(2026, 8, 15)
                )
        ).isFalse();
    }
}