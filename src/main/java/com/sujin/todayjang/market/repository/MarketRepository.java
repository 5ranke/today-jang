package com.sujin.todayjang.market.repository;

import com.sujin.todayjang.market.domain.Market; // Market Entity입니다.
import org.springframework.data.jpa.repository.JpaRepository; // 기본 CRUD 기능을 제공합니다.

public interface MarketRepository
        extends JpaRepository<Market, Long> {

    // 지금은 아무 코드를 작성하지 않아도 됩니다.
    // save(), count(), findAll() 등을 자동으로 사용할 수 있습니다.
}