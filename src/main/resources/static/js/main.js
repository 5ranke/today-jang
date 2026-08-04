// 날짜 선택 버튼들을 모두 찾습니다.
const dateButtons = document.querySelectorAll(".date-buttons button");

// 장날 찾기 버튼을 찾습니다.
const searchButton = document.getElementById("searchButton");

// 현재 위치 검색 버튼을 찾습니다.
const locationButton = document.getElementById("locationButton");

// 현재 선택된 날짜 조건을 저장합니다.
// 처음 화면에서는 '오늘'이 기본값입니다.
let selectedDateType = "today";

// 날짜 버튼마다 클릭 이벤트를 등록합니다.
dateButtons.forEach((button) => {
    button.addEventListener("click", () => {

        // 모든 날짜 버튼의 선택 상태를 제거합니다.
        dateButtons.forEach((item) => {
            item.classList.remove("active");
        });

        // 사용자가 누른 버튼만 선택 상태로 변경합니다.
        button.classList.add("active");

        // data-date-type 속성에 저장된 값을 가져옵니다.
        selectedDateType = button.dataset.dateType;

        // 날짜 선택 버튼이라면 임시로 날짜 선택창을 엽니다.
        if (selectedDateType === "custom") {
            openDatePicker();
        }
    });
});

// 장날 찾기 버튼을 클릭했을 때 검색 함수를 실행합니다.
searchButton.addEventListener("click", searchMarkets);

// 현재 위치 버튼을 클릭했을 때 위치 요청 함수를 실행합니다.
locationButton.addEventListener("click", searchNearbyMarkets);

// 지역과 날짜 조건을 이용해 시장을 검색합니다.
function searchMarkets() {

    // 지역 검색창에 입력한 값을 가져옵니다.
    const region = document.getElementById("region").value.trim();

    // 지역을 입력하지 않았다면 안내 메시지를 보여줍니다.
    if (region === "") {
        alert("여행 지역을 입력해 주세요.");

        // 이후 검색 코드는 실행하지 않습니다.
        return;
    }

    // 지금은 백엔드 API가 없으므로 선택된 값을 알림창으로 확인합니다.
    alert(
        `${region} 지역을 '${selectedDateType}' 조건으로 검색합니다.`
    );

    // 나중에는 아래와 같은 API 요청으로 변경합니다.
    // fetch(`/api/markets?region=${encodeURIComponent(region)}&dateType=${selectedDateType}`)
}

// 날짜 선택창을 여는 임시 함수입니다.
function openDatePicker() {

    // 실제 구현에서는 HTML의 input type="date"를 사용하는 것이 좋습니다.
    alert("날짜 선택 기능은 다음 단계에서 연결할 예정입니다.");
}

// 현재 위치를 이용해 주변 시장을 검색합니다.
function searchNearbyMarkets() {

    // 브라우저가 위치 기능을 지원하는지 확인합니다.
    if (!navigator.geolocation) {
        alert("현재 브라우저에서는 위치 기능을 사용할 수 없습니다.");
        return;
    }

    // 사용자에게 위치 권한을 요청합니다.
    navigator.geolocation.getCurrentPosition(
        (position) => {
            // 사용자의 현재 위도를 가져옵니다.
            const latitude = position.coords.latitude;

            // 사용자의 현재 경도를 가져옵니다.
            const longitude = position.coords.longitude;

            // 현재는 개발 확인을 위해 좌표를 출력합니다.
            console.log("현재 위도:", latitude);
            console.log("현재 경도:", longitude);

            alert("현재 위치를 확인했습니다.");
        },
        () => {
            // 위치 권한을 거부하거나 위치를 가져오지 못한 경우입니다.
            alert("현재 위치를 확인할 수 없습니다.");
        }
    );
}