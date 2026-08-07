package com.sujin.todayjang.market.service;

import java.time.LocalDate; // 날짜를 다루기 위해 사용합니다.

public class MarketDayCalculator {

    public boolean isOpenOn(String openingCycle, LocalDate date) {

        // "매일"인 시장은 날짜와 상관없이 항상 운영합니다.
        if ("매일".equals(openingCycle)) {
            return true;
        }

        // 방문 날짜의 일(day)을 가져옵니다.
        // 예: 2026-08-27 → 27
        int day = date.getDayOfMonth();

        // "2일+7일" → ["2일", "7일"]로 나눕니다.
        String[] cycles = openingCycle.split("\\+");

        // 각 장날 규칙을 하나씩 검사합니다.
        for (String cycle : cycles) {

            // "2일"에서 "일"을 제거합니다.
            String numberText = cycle
                    .replace("일", "")
                    .trim();

            // "2" → 2로 변환합니다.
            int marketDay = Integer.parseInt(numberText);

            // 해당 날짜가 이 규칙에 맞는지 확인합니다.
            if (matches(day, marketDay)) {
                return true;
            }
        }

        // 어떤 규칙에도 해당하지 않는 날짜입니다.
        return false;
    }

    private boolean matches(int day, int marketDay) {

        /*
         * 10일 규칙은 특별하게 처리합니다.
         *
         * 10일 → 10, 20, 30
         */
        if (marketDay == 10) {
            return day % 10 == 0;
        }

        /*
         * 나머지 규칙은 끝자리가 같은지 확인합니다.
         *
         * 2일 → 2, 12, 22
         * 7일 → 7, 17, 27
         */
        return day % 10 == marketDay;
    }
}