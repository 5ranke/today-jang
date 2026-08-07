package com.sujin.todayjang.market.csv;

import org.apache.commons.csv.CSVRecord; // CSV의 한 행을 읽기 위해 사용합니다.

public class MarketCsvRow {

    // CSV의 시장명 값을 그대로 저장합니다.
    private final String name;

    // CSV의 시장유형 값을 그대로 저장합니다.
    private final String marketType;

    // CSV의 소재지도로명주소 값을 그대로 저장합니다.
    private final String roadAddress;

    // CSV의 소재지지번주소 값을 그대로 저장합니다.
    private final String lotAddress;

    // CSV의 시장개설주기를 그대로 저장합니다.
    private final String openingCycle;

    // 아직 문자열 상태의 위도입니다.
    private final String latitude;

    // 아직 문자열 상태의 경도입니다.
    private final String longitude;

    // 아직 문자열 상태의 점포수입니다.
    private final String storeCount;

    // 취급품목 원본 문자열입니다.
    private final String products;

    // 홈페이지 주소입니다.
    private final String homepageUrl;

    // CSV의 Y/N 값을 그대로 저장합니다.
    private final String publicToiletYn;

    // CSV의 Y/N 값을 그대로 저장합니다.
    private final String parkingYn;

    // 아직 문자열 상태의 개설연도입니다.
    private final String establishedYear;

    // 전화번호입니다.
    private final String phoneNumber;

    // 아직 문자열 상태의 데이터기준일자입니다.
    private final String referenceDate;

    public MarketCsvRow(
            String name,
            String marketType,
            String roadAddress,
            String lotAddress,
            String openingCycle,
            String latitude,
            String longitude,
            String storeCount,
            String products,
            String homepageUrl,
            String publicToiletYn,
            String parkingYn,
            String establishedYear,
            String phoneNumber,
            String referenceDate
    ) {
        this.name = name;
        this.marketType = marketType;
        this.roadAddress = roadAddress;
        this.lotAddress = lotAddress;
        this.openingCycle = openingCycle;
        this.latitude = latitude;
        this.longitude = longitude;
        this.storeCount = storeCount;
        this.products = products;
        this.homepageUrl = homepageUrl;
        this.publicToiletYn = publicToiletYn;
        this.parkingYn = parkingYn;
        this.establishedYear = establishedYear;
        this.phoneNumber = phoneNumber;
        this.referenceDate = referenceDate;
    }

    /**
     * CSVRecord 한 줄을 MarketCsvRow 객체로 변환합니다.
     */
    public static MarketCsvRow from(CSVRecord record) {

        return new MarketCsvRow(
                record.get("시장명"),
                record.get("시장유형"),
                record.get("소재지도로명주소"),
                record.get("소재지지번주소"),
                record.get("시장개설주기"),
                record.get("위도"),
                record.get("경도"),
                record.get("점포수"),
                record.get("취급품목"),
                record.get("홈페이지주소"),
                record.get("공중화장실보유여부"),
                record.get("주차장보유여부"),
                record.get("개설연도"),
                record.get("전화번호"),
                record.get("데이터기준일자")
        );
    }

    public String getName() {
        return name;
    }

    public String getMarketType() {
        return marketType;
    }

    public String getRoadAddress() {
        return roadAddress;
    }

    public String getLotAddress() {
        return lotAddress;
    }

    public String getOpeningCycle() {
        return openingCycle;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getStoreCount() {
        return storeCount;
    }

    public String getProducts() {
        return products;
    }

    public String getHomepageUrl() {
        return homepageUrl;
    }

    public String getPublicToiletYn() {
        return publicToiletYn;
    }

    public String getParkingYn() {
        return parkingYn;
    }

    public String getEstablishedYear() {
        return establishedYear;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getReferenceDate() {
        return referenceDate;
    }
}