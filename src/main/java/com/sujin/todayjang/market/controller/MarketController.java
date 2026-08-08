package com.sujin.todayjang.market.controller;

import com.sujin.todayjang.market.dto.MarketDetailResponse;
import com.sujin.todayjang.market.dto.MarketRangeSearchResponse;
import com.sujin.todayjang.market.dto.MarketSearchResponse;
import com.sujin.todayjang.market.service.MarketSearchService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/markets")
public class MarketController {

    private final MarketSearchService marketSearchService;

    public MarketController(
            MarketSearchService marketSearchService
    ) {
        this.marketSearchService = marketSearchService;
    }

    @GetMapping
    public List<MarketSearchResponse> searchMarkets(

            @RequestParam String province,

            @RequestParam String cityCounty,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate date
    ) {

        return marketSearchService.search(
                province,
                cityCounty,
                date
        );
    }

    @GetMapping("/range")
    public List<MarketRangeSearchResponse> searchMarketsByRange(

            @RequestParam String province,

            @RequestParam String cityCounty,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate startDate,

            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate endDate
    ) {

        return marketSearchService.searchRange(
                province,
                cityCounty,
                startDate,
                endDate
        );
    }

    @GetMapping("/{id}")
    public MarketDetailResponse getMarket(
            @PathVariable Long id
    ) {
        return marketSearchService.getMarket(id);
    }
}