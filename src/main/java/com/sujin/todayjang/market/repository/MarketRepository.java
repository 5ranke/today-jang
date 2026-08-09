package com.sujin.todayjang.market.repository;

import com.sujin.todayjang.market.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByProvince(String province);

    // DB에 저장된 도/광역시 목록을 중복 없이 조회합니다.
    @Query("""
            SELECT DISTINCT m.province
            FROM Market m
            ORDER BY m.province
            """)
    List<String> findDistinctProvinces();

}
