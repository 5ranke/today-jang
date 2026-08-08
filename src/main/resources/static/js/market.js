// 시장 검색 API를 호출합니다.
export async function searchMarketsApi(province, cityCounty, date) {

    const url =
        `/api/markets`
        + `?province=${encodeURIComponent(province)}`
        + `&cityCounty=${encodeURIComponent(cityCounty)}`
        + `&date=${date}`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("시장 검색에 실패했습니다.");
    }

    return response.json();
}

export async function searchMarketsByRangeApi(
    province,
    cityCounty,
    startDate,
    endDate
) {

    const url =
        `/api/markets/range`
        + `?province=${encodeURIComponent(province)}`
        + `&cityCounty=${encodeURIComponent(cityCounty)}`
        + `&startDate=${startDate}`
        + `&endDate=${endDate}`;

    const response = await fetch(url);

    if (!response.ok) {
        throw new Error("기간별 시장 검색에 실패했습니다.");
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

export function renderRangeMarkets(
    markets,
    marketList,
    sectionTitle
) {

    marketList.innerHTML = "";

    sectionTitle.textContent = "이번 주 열리는 장";

    if (markets.length === 0) {

        marketList.innerHTML = `
            <div class="empty-result">
                이번 주에 열리는 시장이 없어요.
            </div>
        `;

        return;
    }

    markets.forEach((market) => {

        const card = createRangeMarketCard(market);

        marketList.appendChild(card);
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

        <p>${market.marketType} · ${market.openingCycle}</p>

        <p>${market.roadAddress ?? "주소 정보 없음"}</p>

        <div class="tags">
            ${createTags(market)}
        </div>
    `;

    return card;
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

function createRangeMarketCard(market) {

    const card = document.createElement("article");

    card.className = "market-card";

    const isAlwaysOpen =
        market.openingCycle === "매일";

    const dates = market.openDates
        .map(formatDisplayDate)
        .join(", ");

    card.innerHTML = `
        <span class="badge ${isAlwaysOpen ? "badge-always" : "badge-five-day"}">
            ${isAlwaysOpen ? "매일 열려요" : "이번 주 장날"}
        </span>

        <h3>${market.name}</h3>

        <p class="market-date">
            ${isAlwaysOpen ? "이번 주 매일 방문 가능" : dates}
        </p>

        <p>
            ${market.marketType} · ${market.openingCycle}
        </p>

        <p>
            ${market.roadAddress ?? "주소 정보 없음"}
        </p>

        <div class="tags">
            ${createTags(market)}
        </div>
    `;

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