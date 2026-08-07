package com.sujin.todayjang.market.domain; // Market 도메인 클래스가 위치한 패키지입니다.

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate; // 날짜만 저장하기 위해 사용합니다.

@Getter
@Entity // 이 클래스가 DB 테이블과 연결되는 JPA Entity임을 나타냅니다.
@Table(
        name = "markets", // 연결할 테이블 이름입니다.
        indexes = {
                @Index(
                        name = "idx_market_region", // 인덱스 이름입니다.
                        columnList = "province, city_county" // 지역 검색에 사용할 컬럼입니다.
                )
        }
)
public class Market {

    @Id // markets 테이블의 기본 키입니다.
    @GeneratedValue(strategy = GenerationType.IDENTITY) // PostgreSQL이 id를 자동 증가시킵니다.
    private Long id;

    @Column(nullable = false, length = 100) // 시장명은 반드시 존재하도록 합니다.
    private String name;

    @Column(nullable = false, length = 30) // 상설장, 5일장 등의 시장 유형입니다.
    private String marketType;

    @Column(nullable = false, length = 30) // 강원특별자치도, 서울특별시 등의 광역 지역입니다.
    private String province;

    @Column(nullable = false, length = 30) // 강릉시, 고성군, 종로구 등의 시·군·구입니다.
    private String cityCounty;

    @Column(length = 255) // 일부 데이터가 없기 때문에 null을 허용합니다.
    private String roadAddress;

    @Column(length = 255) // 지번주소 역시 일부 시장에는 없을 수 있습니다.
    private String lotAddress;

    @Column(nullable = false, length = 50) // "매일", "2일+7일" 같은 장날 규칙을 저장합니다.
    private String openingCycle;

    // 일부 시장에는 좌표가 없으므로 기본형 double 대신 Double을 사용합니다.
    private Double latitude;

    // 경도 역시 null이 가능하므로 Double을 사용합니다.
    private Double longitude;

    // 점포 수 데이터가 없을 수 있으므로 Integer를 사용합니다.
    private Integer storeCount;

    @Column(columnDefinition = "TEXT") // 취급품목은 문자열이 길 수 있어 TEXT 타입으로 저장합니다.
    private String products;

    @Column(nullable = false) // CSV의 Y/N 값을 boolean 값으로 변환해 저장합니다.
    private Boolean hasPublicToilet;

    @Column(nullable = false) // 주차 가능 여부를 true/false로 저장합니다.
    private Boolean hasParking;

    // 개설연도가 없는 데이터가 있기 때문에 Integer를 사용합니다.
    private Integer establishedYear;

    @Column(length = 30) // 전화번호는 숫자 계산을 하지 않으므로 String으로 저장합니다.
    private String phoneNumber;

    @Column(length = 500) // 홈페이지 URL은 길어질 수 있으므로 넉넉하게 설정합니다.
    private String homepageUrl;

    @Column(nullable = false) // 공공데이터가 언제 기준인지 기록합니다.
    private LocalDate referenceDate;

    protected Market() {
        // JPA가 Entity 객체를 생성할 때 필요한 기본 생성자입니다.
    }

    public Market(
            String name,
            String marketType,
            String province,
            String cityCounty,
            String roadAddress,
            String lotAddress,
            String openingCycle,
            Double latitude,
            Double longitude,
            Integer storeCount,
            String products,
            Boolean hasPublicToilet,
            Boolean hasParking,
            Integer establishedYear,
            String phoneNumber,
            String homepageUrl,
            LocalDate referenceDate
    ) {
        this.name = name; // 시장명을 저장합니다.
        this.marketType = marketType; // 시장 유형을 저장합니다.
        this.province = province; // 도·광역시 정보를 저장합니다.
        this.cityCounty = cityCounty; // 시·군·구 정보를 저장합니다.
        this.roadAddress = roadAddress; // 도로명주소를 저장합니다.
        this.lotAddress = lotAddress; // 지번주소를 저장합니다.
        this.openingCycle = openingCycle; // 장날 규칙을 저장합니다.
        this.latitude = latitude; // 위도를 저장합니다.
        this.longitude = longitude; // 경도를 저장합니다.
        this.storeCount = storeCount; // 점포 수를 저장합니다.
        this.products = products; // 취급품목을 저장합니다.
        this.hasPublicToilet = hasPublicToilet; // 화장실 여부를 저장합니다.
        this.hasParking = hasParking; // 주차장 여부를 저장합니다.
        this.establishedYear = establishedYear; // 개설연도를 저장합니다.
        this.phoneNumber = phoneNumber; // 전화번호를 저장합니다.
        this.homepageUrl = homepageUrl; // 홈페이지 주소를 저장합니다.
        this.referenceDate = referenceDate; // 데이터 기준 날짜를 저장합니다.
    }
}