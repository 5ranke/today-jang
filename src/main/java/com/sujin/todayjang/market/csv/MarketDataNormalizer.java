package com.sujin.todayjang.market.csv;

import java.time.LocalDate; // 날짜 데이터를 표현하기 위해 사용합니다.
import java.time.format.DateTimeParseException; // 잘못된 날짜 형식을 처리합니다.

public final class MarketDataNormalizer {

    /**
     * 객체를 생성할 필요가 없는 유틸리티 클래스입니다.
     */
    private MarketDataNormalizer() {
    }

    /**
     * 문자열이 null 또는 빈 문자열인지 확인합니다.
     */
    public static boolean isBlank(String value) {

        // null이거나 공백만 있다면 true입니다.
        return value == null || value.isBlank();
    }

    /**
     * 빈 문자열을 null로 바꿉니다.
     */
    public static String emptyToNull(String value) {

        // null이면 그대로 null을 반환합니다.
        if (value == null) {
            return null;
        }

        // 앞뒤 공백을 제거합니다.
        String trimmed = value.trim();

        // 빈 문자열이면 null로 통일합니다.
        if (trimmed.isEmpty()) {
            return null;
        }

        // 값이 존재하면 공백을 제거한 문자열을 반환합니다.
        return trimmed;
    }

    /**
     * Y/N 문자열을 Boolean으로 변환합니다.
     */
    public static Boolean toBoolean(String value) {

        // 데이터가 없으면 null을 반환합니다.
        if (isBlank(value)) {
            return null;
        }

        // 앞뒤 공백을 제거하고 대문자로 변경합니다.
        String normalized = value.trim().toUpperCase();

        // Y는 true입니다.
        if ("Y".equals(normalized)) {
            return true;
        }

        // N은 false입니다.
        if ("N".equals(normalized)) {
            return false;
        }

        // Y/N이 아닌 값이 들어오면 데이터 오류로 판단합니다.
        throw new IllegalArgumentException(
                "Y/N으로 변환할 수 없는 값입니다: " + value
        );
    }

    /**
     * 문자열을 Double로 변환합니다.
     *
     * 위도와 경도에서 사용합니다.
     */
    public static Double toDouble(String value) {

        // 값이 없다면 null입니다.
        if (isBlank(value)) {
            return null;
        }

        try {

            // 문자열을 실수로 변환합니다.
            return Double.parseDouble(value.trim());

        } catch (NumberFormatException e) {

            // 숫자가 아닌 값이 들어온 경우 어떤 값 때문인지 알려줍니다.
            throw new IllegalArgumentException(
                    "Double로 변환할 수 없는 값입니다: " + value,
                    e
            );
        }
    }

    /**
     * 문자열을 Integer로 변환합니다.
     *
     * 점포수, 개설연도 등에 사용합니다.
     */
    public static Integer toInteger(String value) {

        // 값이 없다면 null을 반환합니다.
        if (isBlank(value)) {
            return null;
        }

        try {

            // CSV에서 1980.0처럼 들어오는 경우도 처리하기 위해
            // 먼저 Double로 변환합니다.
            double number = Double.parseDouble(value.trim());

            // 소수점이 있는 실제 값이라면 잘못된 데이터이므로 막습니다.
            if (number % 1 != 0) {
                throw new IllegalArgumentException(
                        "정수로 변환할 수 없는 값입니다: " + value
                );
            }

            // 1980.0 → 1980으로 변환합니다.
            return (int) number;

        } catch (NumberFormatException e) {

            throw new IllegalArgumentException(
                    "Integer로 변환할 수 없는 값입니다: " + value,
                    e
            );
        }
    }

    /**
     * 문자열을 LocalDate로 변환합니다.
     *
     * 2025-11-10 형태를 처리합니다.
     */
    public static LocalDate toLocalDate(String value) {

        // 날짜가 없으면 null입니다.
        if (isBlank(value)) {
            return null;
        }

        try {

            // yyyy-MM-dd 형식의 문자열을 날짜 객체로 변환합니다.
            return LocalDate.parse(value.trim());

        } catch (DateTimeParseException e) {

            throw new IllegalArgumentException(
                    "날짜로 변환할 수 없는 값입니다: " + value,
                    e
            );
        }
    }
}