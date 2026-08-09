import { loadProvinces } from "./region.js";

import {
    searchMarketsApi,
    searchMarketsByRangeApi,
    searchNearbyMarketsApi,
    renderMarkets,
    renderRangeMarkets,
    renderNearbyMarkets,
    formatDisplayDate
} from "./market.js";


const provinceSelect = document.getElementById("province");

const dateButtons =
    document.querySelectorAll(".date-buttons button");

const marketTypeButtons =
    document.querySelectorAll(".market-type-buttons button");

const searchButton =
    document.getElementById("searchButton");

const locationButton =
    document.getElementById("locationButton");

const marketList =
    document.getElementById("marketList");

const customDateInput =
    document.getElementById("customDate");

const sectionTitle =
    document.querySelector(".section-title");

let selectedDateType = "today";

let selectedMarketType = "five-day";

let customSelectedDate = null;


initialize();


async function initialize() {

    try {
        const provinces = await loadProvinces();

        provinces.forEach(province => {

            const option = document.createElement("option");

            option.value = province;
            option.textContent = province;

            provinceSelect.appendChild(option);
        });

    } catch (error) {
        console.error(error);
    }
}


dateButtons.forEach(button => {

    button.addEventListener("click", () => {

        dateButtons.forEach(item =>
            item.classList.remove("active")
        );

        button.classList.add("active");

        selectedDateType = button.dataset.dateType;

        if (selectedDateType === "custom") {
            customDateInput.hidden = false;
        } else {
            customDateInput.hidden = true;
        }
    });
});

marketTypeButtons.forEach(button => {

    button.addEventListener("click", () => {

        marketTypeButtons.forEach(item =>
            item.classList.remove("active")
        );

        button.classList.add("active");
        selectedMarketType = button.dataset.marketType;
    });
});

customDateInput.addEventListener("change", () => {

    customSelectedDate =
        customDateInput.value;
});


searchButton.addEventListener("click", async () => {

    const province = provinceSelect.value;

    if (!province) {
        alert("여행 지역을 선택해 주세요.");
        return;
    }

    try {

        // 이번 주 검색
        if (selectedDateType === "week") {

            const { startDate, endDate } =
                getThisWeekRange();

            const markets =
                await searchMarketsByRangeApi(
                    province,
                    selectedMarketType,
                    startDate,
                    endDate
                );

            renderRangeMarkets(
                markets,
                marketList,
                sectionTitle
            );

            return;
        }


        // 오늘 / 내일 / 날짜 선택 검색
        const date = getSelectedDate();

        if (!date) {
            alert("방문 날짜를 선택해 주세요.");
            return;
        }

        const markets =
            await searchMarketsApi(
                province,
                selectedMarketType,
                date
            );

        renderMarkets(
            markets,
            date,
            marketList
        );

        sectionTitle.textContent =
            `${formatDisplayDate(date)} 열리는 장`;

    } catch (error) {

        console.error(error);

        alert("시장 정보를 불러오지 못했습니다.");
    }
});

locationButton.addEventListener(
    "click",
    searchNearbyMarkets
);


function getSelectedDate() {

    const today = new Date();

    if (selectedDateType === "today") {
        return formatDate(today);
    }

    if (selectedDateType === "tomorrow") {

        const tomorrow = new Date(today);

        tomorrow.setDate(
            today.getDate() + 1
        );

        return formatDate(tomorrow);
    }

    if (selectedDateType === "custom") {
        return customSelectedDate;
    }

    return null;
}


function formatDate(date) {

    const year = date.getFullYear();

    const month =
        String(date.getMonth() + 1).padStart(2, "0");

    const day =
        String(date.getDate()).padStart(2, "0");

    return `${year}-${month}-${day}`;
}

function getThisWeekRange() {

    const today = new Date();

    const endDate = new Date(today);

    /*
     * getDay()
     *
     * 일요일 = 0
     * 월요일 = 1
     * ...
     * 토요일 = 6
     */

    const dayOfWeek = today.getDay();

    // 오늘부터 일요일까지 남은 날짜 수입니다.
    const daysUntilSunday =
        dayOfWeek === 0
            ? 0
            : 7 - dayOfWeek;

    endDate.setDate(
        today.getDate() + daysUntilSunday
    );

    return {
        startDate: formatDate(today),
        endDate: formatDate(endDate)
    };
}

function searchNearbyMarkets() {

    if (!navigator.geolocation) {
        alert(
            "현재 브라우저에서는 위치 기능을 사용할 수 없습니다."
        );
        return;
    }

    navigator.geolocation.getCurrentPosition(

        async (position) => {

            const latitude =
                position.coords.latitude;

            const longitude =
                position.coords.longitude;

            const today =
                formatDate(new Date());

            try {

                const markets =
                    await searchNearbyMarketsApi(
                        latitude,
                        longitude,
                        today
                    );

                renderNearbyMarkets(
                    markets,
                    marketList,
                    sectionTitle
                );

            } catch (error) {

                console.error(error);

                alert(
                    "주변 시장 정보를 불러오지 못했습니다."
                );
            }
        },

        () => {
            alert(
                "현재 위치를 확인할 수 없습니다. 위치 권한을 확인해 주세요."
            );
        }
    );
}
