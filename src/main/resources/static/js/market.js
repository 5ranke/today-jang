// 시장 검색 API를 호출합니다.
export async function searchMarketsApi(province, marketType, date) {

    const url =
        `/api/markets`
        + `?province=${encodeURIComponent(province)}`
        + `&marketType=${encodeURIComponent(marketType)}`
        + `&date=${date}`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("시장 검색에 실패했습니다.");
    }

    return response.json();
}

export async function searchNearbyMarketsApi(
    latitude,
    longitude,
    date
) {

    const url =
        `/api/markets/nearby`
        + `?latitude=${latitude}`
        + `&longitude=${longitude}`
        + `&date=${date}`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("주변 시장 검색에 실패했습니다.");
    }

    return response.json();
}

// 검색 결과 카드를 만듭니다.
export function renderMarkets(markets, date, marketList) {

    marketList.innerHTML = "";

    if (markets.length === 0) {
        marketList.innerHTML = `
            <div class="empty-result">
                선택한 지역과 날짜에 열리는 시장이 없어요.
            </div>
        `;
        return;
    }

    markets.forEach((market) => {
        marketList.appendChild(createMarketCard(market, date));
    });
}

function createMarketCard(market, date) {

    const card = document.createElement("article");

    card.className = "market-card";

    const isAlwaysOpen = market.openingCycle === "매일";

    card.innerHTML = `
        <span class="badge ${isAlwaysOpen ? "badge-always" : "badge-five-day"}">
            ${isAlwaysOpen ? "매일 열려요" : "이날 장이 열려요"}
        </span>

        <h3>${market.name}</h3>

        <p class="market-date">
            ${formatDisplayDate(date)}
        </p>

        <p>${createMarketSummary(market)}</p>

        <p>${market.roadAddress ?? market.lotAddress ?? "주소 정보 없음"}</p>

        <div class="tags">
            ${createTags(market)}
        </div>

        <div class="card-actions">
            <button
                type="button"
                class="detail-button"
            >
                상세보기
            </button>

            <button
                type="button"
                class="copy-address-button"
            >
                주소 복사
            </button>
        </div>
    `;

    const detailButton =
        card.querySelector(".detail-button");

    detailButton.addEventListener("click", () => {
        window.location.href =
            `/market-detail.html?id=${market.id}`;
    });

    const copyAddressButton =
        card.querySelector(".copy-address-button");

    copyAddressButton.addEventListener("click", () => {
        copyMarketAddress(market, copyAddressButton);
    });

    return card;
}

function createMarketSummary(market) {

    const summary = [market.marketType, market.openingCycle];

    if (market.storeCount != null) {
        summary.push(
            `점포 ${market.storeCount.toLocaleString("ko-KR")}개`
        );
    }

    return summary.join(" · ");
}

function createTags(market) {

    const tags = [];

    if (market.products) {
        market.products
            .split("+")
            .slice(0, 4)
            .forEach(product => {
                tags.push(
                    `<span class="tag">${product.trim()}</span>`
                );
            });
    }

    if (market.hasParking) {
        tags.push(`<span class="tag">주차 가능</span>`);
    }

    return tags.join("");
}

function createNearbyMarketCard(market) {

    const card =
        document.createElement("article");

    card.className = "market-card";

    const isAlwaysOpen =
        market.openingCycle === "매일";

    card.innerHTML = `
        <span class="badge ${isAlwaysOpen ? "badge-always" : "badge-five-day"}">
            ${isAlwaysOpen ? "매일 열려요" : "오늘 장이 열려요"}
        </span>

        <h3>${market.name}</h3>

        <p class="market-distance">
            📍 ${formatDistance(market.distanceKm)} 떨어져 있어요
        </p>

        <p>
            ${createMarketSummary(market)}
        </p>

        <p>
            ${market.roadAddress ?? market.lotAddress ?? "주소 정보 없음"}
        </p>

        <div class="tags">
            ${createTags(market)}
        </div>

        <div class="card-actions">
            <button
                type="button"
                class="detail-button"
            >
                상세보기
            </button>

            <button
                type="button"
                class="copy-address-button"
            >
                주소 복사
            </button>
        </div>
    `;

    const detailButton =
        card.querySelector(".detail-button");

    detailButton.addEventListener("click", () => {

        window.location.href =
            `/market-detail.html?id=${market.id}`;
    });

    const copyAddressButton =
        card.querySelector(".copy-address-button");

    copyAddressButton.addEventListener("click", () => {
        copyMarketAddress(market, copyAddressButton);
    });

    return card;
}


export function formatDisplayDate(dateString) {

    const date = new Date(`${dateString}T00:00:00`);

    return new Intl.DateTimeFormat("ko-KR", {
        month: "long",
        day: "numeric",
        weekday: "long"
    }).format(date);
}

function formatDistance(distanceKm) {

    // 1km보다 가까우면 미터로 표시합니다.
    if (distanceKm < 1) {
        return `${Math.round(distanceKm * 1000)}m`;
    }

    // 1km 이상이면 소수점 첫째 자리까지 표시합니다.
    return `${distanceKm.toFixed(1)}km`;
}

export function renderNearbyMarkets(
    markets,
    marketList,
    sectionTitle
) {

    marketList.innerHTML = "";

    sectionTitle.textContent =
        "내 주변 오늘 열리는 5일장";

    if (markets.length === 0) {

        marketList.innerHTML = `
            <div class="empty-result">
                현재 위치에서 20km 이내에
                오늘 열리는 5일장이 없어요.
            </div>
        `;

        return;
    }

    markets.forEach(market => {

        const card =
            createNearbyMarketCard(market);

        marketList.appendChild(card);
    });
}

async function copyMarketAddress(market, button) {

    const address =
        market.roadAddress
        ?? market.lotAddress;

    if (!address) {
        alert("복사할 주소 정보가 없습니다.");
        return;
    }

    try {
        await navigator.clipboard.writeText(address);

        const originalText = button.textContent;

        button.textContent = "복사됨";

        setTimeout(() => {
            button.textContent = originalText;
        }, 1500);

    } catch (error) {
        console.error(error);

        alert("주소를 복사하지 못했습니다.");
    }
}
