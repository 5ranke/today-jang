const dateButtons = document.querySelectorAll(".date-buttons button");

const searchButton = document.getElementById("searchButton");

const locationButton = document.getElementById("locationButton");

const provinceSelect = document.getElementById("province");

const cityCountySelect = document.getElementById("cityCounty");

const marketList = document.getElementById("marketList");

const sectionTitle = document.querySelector(".section-title");

const customDateInput =
    document.getElementById("customDate");

let customSelectedDate = null;

let selectedDateType = "today";


// 페이지가 열리면 도/광역시 목록을 조회합니다.
loadProvinces();


async function loadProvinces() {

    try {
        const response = await fetch("/api/regions/provinces");

        if (!response.ok) {
            throw new Error("지역 목록 조회에 실패했습니다.");
        }

        const provinces = await response.json();

        provinces.forEach((province) => {

            const option = document.createElement("option");

            option.value = province;
            option.textContent = province;

            provinceSelect.appendChild(option);
        });

    } catch (error) {
        console.error(error);
        alert("지역 목록을 불러오지 못했습니다.");
    }
}


// 도/광역시를 선택하면 시·군·구 목록을 조회합니다.
provinceSelect.addEventListener("change", async () => {

    const province = provinceSelect.value;

    cityCountySelect.innerHTML =
        '<option value="">시·군·구 선택</option>';

    if (!province) {
        cityCountySelect.disabled = true;
        return;
    }

    try {
        const response = await fetch(
            `/api/regions/cities?province=${encodeURIComponent(province)}`
        );

        if (!response.ok) {
            throw new Error("시·군·구 목록 조회에 실패했습니다.");
        }

        const cities = await response.json();

        cities.forEach((city) => {

            const option = document.createElement("option");

            option.value = city;
            option.textContent = city;

            cityCountySelect.appendChild(option);
        });

        cityCountySelect.disabled = false;

    } catch (error) {
        console.error(error);
        alert("시·군·구 목록을 불러오지 못했습니다.");
    }
});


// 날짜 버튼 클릭 처리
dateButtons.forEach((button) => {

    button.addEventListener("click", () => {

        dateButtons.forEach((item) => {
            item.classList.remove("active");
        });

        button.classList.add("active");

        selectedDateType = button.dataset.dateType;

        if (selectedDateType === "custom") {
            openDatePicker();
        }
    });
});


// 장날 찾기 버튼
searchButton.addEventListener("click", searchMarkets);


// 현재 위치 버튼
locationButton.addEventListener("click", searchNearbyMarkets);


async function searchMarkets() {

    const province = provinceSelect.value;
    const cityCounty = cityCountySelect.value;

    if (!province) {
        alert("도/광역시를 선택해 주세요.");
        return;
    }

    if (!cityCounty) {
        alert("시·군·구를 선택해 주세요.");
        return;
    }

    const date = getSelectedDate();

    if (!date) {
        alert("방문 날짜를 선택해 주세요.");
        return;
    }

    if (selectedDateType === "week") {
        alert("이번 주 검색은 다음 단계에서 구현할 예정입니다.");
        return;
    }

    try {

        const url =
            `/api/markets`
            + `?province=${encodeURIComponent(province)}`
            + `&cityCounty=${encodeURIComponent(cityCounty)}`
            + `&date=${date}`;

        const response = await fetch(url);

        if (!response.ok) {
            throw new Error("시장 검색에 실패했습니다.");
        }

        const markets = await response.json();

        renderMarkets(markets, date);

    } catch (error) {

        console.error(error);

        alert("시장 정보를 불러오지 못했습니다.");
    }
}

function renderMarkets(markets, date) {

    marketList.innerHTML = "";

    sectionTitle.textContent =
        `${formatDisplayDate(date)} 열리는 장`;

    if (markets.length === 0) {

        marketList.innerHTML = `
            <div class="empty-result">
                선택한 지역과 날짜에 열리는 시장이 없어요.
            </div>
        `;

        return;
    }

    markets.forEach((market) => {

        const card = createMarketCard(market, date);

        marketList.appendChild(card);
    });
}

function createMarketCard(market, date) {

    const article = document.createElement("article");

    article.className = "market-card";

    const isAlwaysOpen =
        market.openingCycle === "매일";

    const badgeText =
        isAlwaysOpen
            ? "매일 열려요"
            : "이날 장이 열려요";

    const badgeClass =
        isAlwaysOpen
            ? "badge-always"
            : "badge-five-day";

    const tags = createProductTags(
        market.products,
        market.hasParking
    );

    article.innerHTML = `
        <span class="badge ${badgeClass}">
            ${badgeText}
        </span>

        <h3>${market.name}</h3>

        <p class="market-date">
            ${formatDisplayDate(date)}
        </p>

        <p>
            ${market.marketType} · ${market.openingCycle}
        </p>

        <p>
            ${market.roadAddress ?? "주소 정보 없음"}
        </p>

        <div class="tags">
            ${tags}
        </div>

        <div class="card-actions">
            <button
                type="button"
                data-market-id="${market.id}"
                class="detail-button"
            >
                상세보기
            </button>

            <button
                type="button"
                class="direction-button"
            >
                길찾기
            </button>
        </div>
    `;

    return article;
}

function createProductTags(
    products,
    hasParking
) {

    const tags = [];

    if (products) {

        const productList =
            products.split("+");

        productList
            .slice(0, 4)
            .forEach((product) => {

                tags.push(
                    `<span class="tag">${product.trim()}</span>`
                );
            });
    }

    if (hasParking) {

        tags.push(
            `<span class="tag">주차 가능</span>`
        );
    }

    return tags.join("");
}

function formatDisplayDate(dateString) {

    const date =
        new Date(`${dateString}T00:00:00`);

    return new Intl.DateTimeFormat(
        "ko-KR",
        {
            month: "long",
            day: "numeric",
            weekday: "long"
        }
    ).format(date);
}

function openDatePicker() {

    customDateInput.hidden = false;

    customDateInput.showPicker();
}


function searchNearbyMarkets() {

    if (!navigator.geolocation) {
        alert("현재 브라우저에서는 위치 기능을 사용할 수 없습니다.");
        return;
    }

    navigator.geolocation.getCurrentPosition(
        (position) => {

            const latitude = position.coords.latitude;

            const longitude = position.coords.longitude;

            console.log("현재 위도:", latitude);
            console.log("현재 경도:", longitude);

            alert("현재 위치를 확인했습니다.");
        },

        () => {
            alert("현재 위치를 확인할 수 없습니다.");
        }
    );
}

function getSelectedDate() {

    const today = new Date();

    if (selectedDateType === "today") {
        return formatDate(today);
    }

    if (selectedDateType === "tomorrow") {

        const tomorrow = new Date(today);

        tomorrow.setDate(today.getDate() + 1);

        return formatDate(tomorrow);
    }

    if (selectedDateType === "week") {

        // 이번 주 검색은 아직 별도 API 로직이 없으므로
        // 현재는 오늘 날짜를 기준으로 처리합니다.
        return formatDate(today);
    }

    if (selectedDateType === "custom") {
        return customSelectedDate;
    }
}

function formatDate(date) {

    const year = date.getFullYear();

    const month = String(
        date.getMonth() + 1
    ).padStart(2, "0");

    const day = String(
        date.getDate()
    ).padStart(2, "0");

    return `${year}-${month}-${day}`;
}