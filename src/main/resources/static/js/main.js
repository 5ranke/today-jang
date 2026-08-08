import {
    loadProvinces,
    loadCities
} from "./region.js";

import {
    searchMarketsApi,
    renderMarkets
} from "./market.js";


const provinceSelect = document.getElementById("province");
const cityCountySelect = document.getElementById("cityCounty");

const dateButtons =
    document.querySelectorAll(".date-buttons button");

const searchButton =
    document.getElementById("searchButton");

const marketList =
    document.getElementById("marketList");

let selectedDateType = "today";


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


provinceSelect.addEventListener("change", async () => {

    const province = provinceSelect.value;

    cityCountySelect.innerHTML =
        '<option value="">시·군·구 선택</option>';

    if (!province) {
        cityCountySelect.disabled = true;
        return;
    }

    try {
        const cities = await loadCities(province);

        cities.forEach(city => {

            const option = document.createElement("option");

            option.value = city;
            option.textContent = city;

            cityCountySelect.appendChild(option);
        });

        cityCountySelect.disabled = false;

    } catch (error) {
        console.error(error);
    }
});


dateButtons.forEach(button => {

    button.addEventListener("click", () => {

        dateButtons.forEach(item =>
            item.classList.remove("active")
        );

        button.classList.add("active");

        selectedDateType = button.dataset.dateType;
    });
});


searchButton.addEventListener("click", async () => {

    const province = provinceSelect.value;
    const cityCounty = cityCountySelect.value;

    if (!province || !cityCounty) {
        alert("여행 지역을 선택해 주세요.");
        return;
    }

    const date = getSelectedDate();

    if (!date) {
        alert("방문 날짜를 선택해 주세요.");
        return;
    }

    try {
        const markets = await searchMarketsApi(
            province,
            cityCounty,
            date
        );

        renderMarkets(
            markets,
            date,
            marketList
        );

    } catch (error) {
        console.error(error);
        alert("시장 정보를 불러오지 못했습니다.");
    }
});


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