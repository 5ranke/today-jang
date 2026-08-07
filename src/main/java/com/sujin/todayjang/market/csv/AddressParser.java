package com.sujin.todayjang.market.csv;

public final class AddressParser {

    /**
     * 객체를 만들 필요가 없는 유틸리티 클래스이므로
     * 외부에서 new AddressParser()를 하지 못하도록 막습니다.
     */
    private AddressParser() {
    }

    /**
     * 도로명주소가 존재하면 도로명주소를 사용하고,
     * 없으면 지번주소를 사용합니다.
     */
    public static String selectAddress(
            String roadAddress,
            String lotAddress
    ) {

        // 도로명주소가 존재하면 가장 먼저 사용합니다.
        if (!MarketDataNormalizer.isBlank(roadAddress)) {
            return roadAddress.trim();
        }

        // 도로명주소가 없다면 지번주소를 사용합니다.
        if (!MarketDataNormalizer.isBlank(lotAddress)) {
            return lotAddress.trim();
        }

        // 둘 다 없다면 주소를 추출할 수 없습니다.
        return null;
    }

    /**
     * 주소에서 첫 번째 지역을 추출합니다.
     *
     * 예:
     * 강원특별자치도 강릉시 금성로21
     * → 강원특별자치도
     */
    public static String extractProvince(String address) {

        // 주소가 없으면 null을 반환합니다.
        if (MarketDataNormalizer.isBlank(address)) {
            return null;
        }

        // 공백을 기준으로 주소를 나눕니다.
        String[] parts = address.trim().split("\\s+");

        // 주소의 첫 번째 부분을 가져옵니다.
        String province = parts[0];

        // 데이터에 존재하는 지역명 오타를 정리해서 반환합니다.
        return normalizeProvince(province);
    }

    /**
     * 주소에서 시·군·구를 추출합니다.
     *
     * 예:
     * 강원특별자치도 강릉시 금성로21
     * → 강릉시
     */
    public static String extractCityCounty(String address) {

        // 주소가 없으면 null입니다.
        if (MarketDataNormalizer.isBlank(address)) {
            return null;
        }

        // 주소를 공백 기준으로 분리합니다.
        String[] parts = address.trim().split("\\s+");

        // 첫 번째 값만 존재한다면 시·군·구를 알 수 없습니다.
        if (parts.length < 2) {
            return null;
        }

        // 두 번째 값을 시·군·구로 사용합니다.
        return parts[1];
    }

    /**
     * CSV에 존재하는 잘못된 광역지역명을 정상 값으로 변환합니다.
     */
    private static String normalizeProvince(String province) {

        // 실제 CSV에 존재하는 오타입니다.
        if ("전북특별차치도".equals(province)) {
            return "전북특별자치도";
        }

        // 수정 대상이 아니라면 원본을 그대로 반환합니다.
        return province;
    }
}