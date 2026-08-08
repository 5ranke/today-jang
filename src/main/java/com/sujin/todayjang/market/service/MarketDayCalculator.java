package com.sujin.todayjang.market.service;

import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class MarketDayCalculator {

    public boolean isOpenOn(String openingCycle, LocalDate date) {

        if ("매일".equals(openingCycle)) {
            return true;
        }

        int day = date.getDayOfMonth();

        String[] cycles = openingCycle.split("\\+");

        for (String cycle : cycles) {

            int marketDay = Integer.parseInt(
                    cycle.replace("일", "").trim()
            );

            if (matches(day, marketDay)) {
                return true;
            }
        }

        return false;
    }

    private boolean matches(int day, int marketDay) {

        if (marketDay == 10) {
            return day % 10 == 0;
        }

        return day % 10 == marketDay;
    }
}