import { loadProvinces } from "./region.js";

import {
    searchMarketsApi,
    searchNearbyMarketsApi,
    renderMarkets,
    renderNearbyMarkets,
    formatDisplayDate
} from "./market.js";


const provinceSelect = document.getElementById("province");

const dateButtons =
    document.querySelectorAll(".date-buttons button");

const searchButton =
    document.getElementById("searchButton");

const locationButton =
    document.getElementById("locationButton");

const marketList =
    document.getElementById("marketList");

const customDateInput =
    document.getElementById("customDate");

const includePermanentMarkets =
    document.getElementById("includePermanentMarkets");

const sectionTitle =
    document.querySelector(".section-title");

let selectedDateType = "today";

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

customDateInput.addEventListener("change", () => {

    customSelectedDate =
        customDateInput.value;
});

customDateInput.addEventListener("click", () => {

    if (typeof customDateInput.showPicker === "function") {
        try {
            customDateInput.showPicker();
        } catch (error) {
            console.debug("날짜 선택기를 자동으로 열 수 없습니다.", error);
        }
    }
});


searchButton.addEventListener("click", async () => {

    const province = provinceSelect.value;

    if (!province) {
        alert("여행 지역을 선택해 주세요.");
        return;
    }

    try {

        // 오늘 / 날짜 선택 검색
        const date = getSelectedDate();

        if (!date) {
            alert("방문 날짜를 선택해 주세요.");
            return;
        }

        const markets =
            await searchMarketsApi(
                province,
                includePermanentMarkets.checked
                    ? "all"
                    : "five-day",
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
