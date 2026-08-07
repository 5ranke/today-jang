package com.sujin.todayjang.market.csv;

import com.sujin.todayjang.market.domain.Market; // Market Entity입니다.
import com.sujin.todayjang.market.repository.MarketRepository; // DB 저장을 담당합니다.

import org.apache.commons.csv.CSVFormat; // CSV 형식을 설정합니다.
import org.apache.commons.csv.CSVParser; // CSV 전체를 파싱합니다.
import org.apache.commons.csv.CSVRecord; // CSV 한 줄을 표현합니다.

import org.springframework.boot.CommandLineRunner; // 서버 시작 후 코드를 실행하기 위해 사용합니다.
import org.springframework.core.io.ClassPathResource; // resources 폴더의 파일을 읽습니다.
import org.springframework.stereotype.Component; // Spring Bean으로 등록합니다.
import org.springframework.transaction.annotation.Transactional; // DB 저장을 하나의 작업 단위로 묶습니다.

import java.io.BufferedReader; // 파일을 효율적으로 읽습니다.
import java.io.IOException; // 파일 읽기 오류를 처리합니다.
import java.io.InputStream; // 파일의 바이트 데이터를 읽습니다.
import java.io.InputStreamReader; // 바이트를 문자로 변환합니다.
import java.io.PushbackReader; // UTF-8 BOM을 확인하고 필요하면 되돌리기 위해 사용합니다.
import java.io.Reader; // 문자 입력을 표현합니다.
import java.nio.charset.StandardCharsets; // UTF-8 인코딩을 지정합니다.

@Component // Spring이 이 클래스를 자동으로 생성하고 관리합니다.
public class MarketCsvImporter implements CommandLineRunner {

    // DB에 Market을 저장하기 위한 Repository입니다.
    private final MarketRepository marketRepository;

    // 생성자 주입 방식으로 Repository를 전달받습니다.
    public MarketCsvImporter(MarketRepository marketRepository) {
        this.marketRepository = marketRepository;
    }

    /**
     * Spring Boot가 실행된 후 자동으로 호출됩니다.
     */
    @Override
    @Transactional // CSV 전체 저장 과정을 하나의 트랜잭션으로 처리합니다.
    public void run(String... args) throws Exception {

        /*
         * 서버를 다시 시작할 때마다
         * 동일한 1,393개의 시장이 계속 추가되는 것을 방지합니다.
         */
        if (marketRepository.count() > 0) {

            System.out.println(
                    "[Market CSV] 이미 시장 데이터가 존재하여 Import를 건너뜁니다."
            );

            return;
        }

        // CSV Import를 실행합니다.
        importMarkets();
    }

    /**
     * 실제 CSV 파일을 읽고 Market 데이터를 저장합니다.
     */
    private void importMarkets() throws IOException {

        // resources/data/markets.csv 파일을 찾습니다.
        ClassPathResource resource =
                new ClassPathResource("data/markets.csv");

        // CSV 파일을 UTF-8 방식으로 읽습니다.
        try (
                InputStream inputStream = resource.getInputStream();

                Reader reader = createUtf8BomSafeReader(inputStream);

                CSVParser parser = CSVFormat.DEFAULT
                        .builder()

                        // 첫 번째 줄을 컬럼명으로 사용합니다.
                        .setHeader()

                        // 컬럼명 행은 실제 데이터에서는 제외합니다.
                        .setSkipHeaderRecord(true)

                        // 값 앞뒤 공백을 제거합니다.
                        .setTrim(true)

                        // 빈 줄이 있어도 무시합니다.
                        .setIgnoreEmptyLines(true)

                        // 최종 CSV 설정을 생성합니다.
                        .build()

                        // 위 설정을 이용해 CSV를 읽습니다.
                        .parse(reader)
        ) {

            // 성공적으로 저장한 데이터 개수를 기록합니다.
            int savedCount = 0;

            // CSV의 모든 행을 하나씩 읽습니다.
            for (CSVRecord record : parser) {

                try {

                    // CSV 한 줄을 문자열 기반 객체로 만듭니다.
                    MarketCsvRow row = MarketCsvRow.from(record);

                    // 가공된 Market Entity를 생성합니다.
                    Market market = createMarket(row);

                    // PostgreSQL에 저장합니다.
                    marketRepository.save(market);

                    // 저장 성공 개수를 증가시킵니다.
                    savedCount++;

                } catch (Exception e) {

                    /*
                     * 어떤 CSV 행에서 오류가 발생했는지 알 수 있도록
                     * 행 번호와 오류 내용을 출력합니다.
                     */
                    throw new IllegalStateException(
                            "CSV " + record.getRecordNumber()
                                    + "번째 데이터 처리 중 오류가 발생했습니다.",
                            e
                    );
                }
            }

            // Import 완료 결과를 콘솔에서 확인할 수 있습니다.
            System.out.println(
                    "[Market CSV] 총 "
                            + savedCount
                            + "개의 시장 데이터를 저장했습니다."
            );
        }
    }

    /**
     * MarketCsvRow의 문자열 데이터를
     * 실제 Market Entity로 변환합니다.
     */
    private Market createMarket(MarketCsvRow row) {

        // 도로명주소를 우선 사용하고, 없으면 지번주소를 사용합니다.
        String address = AddressParser.selectAddress(
                row.getRoadAddress(),
                row.getLotAddress()
        );

        // 주소의 첫 번째 부분에서 도/광역시를 추출합니다.
        String province =
                AddressParser.extractProvince(address);

        // 주소의 두 번째 부분에서 시/군/구를 추출합니다.
        String cityCounty =
                AddressParser.extractCityCounty(address);

        /*
         * CSV의 문자열 데이터를 하나씩 변환하여
         * Market Entity를 생성합니다.
         */
        return new Market(
                // 시장명입니다.
                MarketDataNormalizer.emptyToNull(
                        row.getName()
                ),

                // 시장유형입니다.
                MarketDataNormalizer.emptyToNull(
                        row.getMarketType()
                ),

                // 주소에서 추출한 도/광역시입니다.
                province,

                // 주소에서 추출한 시/군/구입니다.
                cityCounty,

                // 도로명주소입니다.
                MarketDataNormalizer.emptyToNull(
                        row.getRoadAddress()
                ),

                // 지번주소입니다.
                MarketDataNormalizer.emptyToNull(
                        row.getLotAddress()
                ),

                // 장날 규칙은 원본 문자열을 그대로 저장합니다.
                MarketDataNormalizer.emptyToNull(
                        row.getOpeningCycle()
                ),

                // 위도를 String → Double로 변환합니다.
                MarketDataNormalizer.toDouble(
                        row.getLatitude()
                ),

                // 경도를 String → Double로 변환합니다.
                MarketDataNormalizer.toDouble(
                        row.getLongitude()
                ),

                // 점포수를 String → Integer로 변환합니다.
                MarketDataNormalizer.toInteger(
                        row.getStoreCount()
                ),

                // 취급품목은 아직 원본 문자열 그대로 저장합니다.
                MarketDataNormalizer.emptyToNull(
                        row.getProducts()
                ),

                // 화장실 Y/N을 Boolean으로 변환합니다.
                MarketDataNormalizer.toBoolean(
                        row.getPublicToiletYn()
                ),

                // 주차장 Y/N을 Boolean으로 변환합니다.
                MarketDataNormalizer.toBoolean(
                        row.getParkingYn()
                ),

                // 개설연도를 Integer로 변환합니다.
                MarketDataNormalizer.toInteger(
                        row.getEstablishedYear()
                ),

                // 전화번호는 문자열 그대로 저장합니다.
                MarketDataNormalizer.emptyToNull(
                        row.getPhoneNumber()
                ),

                // 홈페이지 주소입니다.
                MarketDataNormalizer.emptyToNull(
                        row.getHomepageUrl()
                ),

                // 데이터 기준일을 LocalDate로 변환합니다.
                MarketDataNormalizer.toLocalDate(
                        row.getReferenceDate()
                )
        );
    }

    /**
     * 네 CSV는 UTF-8 BOM 형식이기 때문에
     * 파일 맨 앞의 BOM 문자를 제거해서 읽습니다.
     *
     * BOM이란?
     * UTF-8 파일의 시작 부분에 붙을 수 있는
     * 보이지 않는 특수 문자입니다.
     */
    private Reader createUtf8BomSafeReader(
            InputStream inputStream
    ) throws IOException {

        // UTF-8 문자 스트림으로 변환합니다.
        InputStreamReader inputStreamReader =
                new InputStreamReader(
                        inputStream,
                        StandardCharsets.UTF_8
                );

        // 첫 글자를 읽었다가 필요하면 다시 넣을 수 있는 Reader입니다.
        PushbackReader pushbackReader =
                new PushbackReader(
                        new BufferedReader(inputStreamReader),
                        1
                );

        // 파일의 첫 글자를 읽습니다.
        int firstCharacter = pushbackReader.read();

        /*
         * UTF-8 BOM 값은 \uFEFF입니다.
         * BOM이 아니라면 읽었던 글자를 다시 되돌려놓습니다.
         */
        if (firstCharacter != '\uFEFF'
                && firstCharacter != -1) {

            pushbackReader.unread(firstCharacter);
        }

        // BOM이 제거된 Reader를 반환합니다.
        return pushbackReader;
    }
}