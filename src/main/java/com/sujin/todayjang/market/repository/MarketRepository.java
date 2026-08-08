package com.sujin.todayjang.market.repository;

import com.sujin.todayjang.market.domain.Market;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MarketRepository extends JpaRepository<Market, Long> {

    List<Market> findByProvinceAndCityCounty(
            String province,
            String cityCounty
    );
}