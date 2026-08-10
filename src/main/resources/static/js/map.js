import {
    createMarketCard,
    createNearbyMarketCard
} from "./market.js";

let map;
let markerLayer;
let selectedMarker;

const DEFAULT_MARKER_STYLE = {
    radius: 7,
    color: "#ffffff",
    weight: 2,
    fillColor: "#3478d4",
    fillOpacity: 0.94
};

const SELECTED_MARKER_STYLE = {
    radius: 10,
    color: "#ffffff",
    weight: 3,
    fillColor: "#d46b2c",
    fillOpacity: 1
};

export function renderMarketMap(markets, date, isNearbySearch = false) {

    const mapElement = document.getElementById("marketMap");
    const mapNotice = document.getElementById("mapNotice");

    if (!window.L) {
        mapNotice.textContent =
            "지도를 불러오지 못했습니다. 인터넷 연결을 확인해 주세요.";
        mapNotice.hidden = false;
        return;
    }

    if (!map) {
        map = window.L.map(mapElement, {
            zoomControl: true,
            scrollWheelZoom: true
        });

        window.L.tileLayer(
            "https://tile.openstreetmap.org/{z}/{x}/{y}.png",
            {
                maxZoom: 19,
                attribution:
                    '&copy; <a href="https://www.openstreetmap.org/copyright">OpenStreetMap</a> contributors'
            }
        ).addTo(map);

        markerLayer = window.L.layerGroup().addTo(map);
    }

    markerLayer.clearLayers();
    selectedMarker = null;

    const marketsWithCoordinates = markets.filter(market =>
        Number.isFinite(market.latitude)
        && Number.isFinite(market.longitude)
    );

    const featuredMarket = marketsWithCoordinates.reduce(
        (largest, market) =>
            (market.storeCount ?? -1) > (largest.storeCount ?? -1)
                ? market
                : largest,
        marketsWithCoordinates[0]
    );

    let featuredMarker;

    marketsWithCoordinates.forEach(market => {
        const marker = window.L.circleMarker(
            [market.latitude, market.longitude],
            DEFAULT_MARKER_STYLE
        );

        const card = isNearbySearch
            ? createNearbyMarketCard(market)
            : createMarketCard(market, date);

        card.classList.add("map-market-card");

        marker.bindPopup(card, {
            className: "market-map-popup",
            maxWidth: 320,
            minWidth: 260,
            autoPan: true,
            keepInView: true,
            autoPanPaddingTopLeft: [24, 24],
            autoPanPaddingBottomRight: [24, 24]
        });

        marker.addTo(markerLayer);

        marker.on("click", () => {
            selectMarker(marker);
        });

        marker.on("popupclose", () => {
            clearSelectedMarker(marker);
        });

        if (market === featuredMarket) {
            featuredMarker = marker;
        }
    });

    const missingCoordinateCount =
        markets.length - marketsWithCoordinates.length;

    let resultBounds;

    if (marketsWithCoordinates.length === 0) {
        map.setView([36.3, 127.8], 7);
        mapNotice.textContent = markets.length === 0
            ? "지도에 표시할 검색 결과가 없어요."
            : "검색된 시장에 좌표 정보가 없어 지도에 표시할 수 없어요.";
        mapNotice.hidden = false;
    } else {
        resultBounds = window.L.latLngBounds(
            marketsWithCoordinates.map(market => [
                market.latitude,
                market.longitude
            ])
        );

        map.fitBounds(resultBounds, {
            padding: [36, 36],
            maxZoom: 14,
            animate: false
        });

        mapNotice.textContent = missingCoordinateCount > 0
            ? `좌표 정보가 없는 시장 ${missingCoordinateCount}곳은 지도에서 제외했어요.`
            : "";
        mapNotice.hidden = missingCoordinateCount === 0;
    }

    requestAnimationFrame(() => {
        map.invalidateSize();

        if (featuredMarker) {
            selectMarker(featuredMarker);
            featuredMarker.openPopup();

            requestAnimationFrame(() => {
                const popupElement =
                    featuredMarker.getPopup().getElement();

                const popupTopPadding = Math.min(
                    (popupElement?.offsetHeight ?? 280) + 36,
                    mapElement.clientHeight - 72
                );

                map.fitBounds(resultBounds, {
                    paddingTopLeft: [36, popupTopPadding],
                    paddingBottomRight: [36, 36],
                    maxZoom: 14,
                    animate: false
                });
            });
        }
    });
}

function selectMarker(marker) {

    if (selectedMarker && selectedMarker !== marker) {
        selectedMarker.setStyle(DEFAULT_MARKER_STYLE);
    }

    marker.setStyle(SELECTED_MARKER_STYLE);
    marker.bringToFront();
    selectedMarker = marker;
}

function clearSelectedMarker(marker) {

    if (selectedMarker !== marker) {
        return;
    }

    marker.setStyle(DEFAULT_MARKER_STYLE);
    selectedMarker = null;
}
