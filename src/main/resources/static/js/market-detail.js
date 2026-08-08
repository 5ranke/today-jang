const marketName =
    document.getElementById("marketName");

const marketTypeBadge =
    document.getElementById("marketTypeBadge");

const marketAddress =
    document.getElementById("marketAddress");

const marketType =
    document.getElementById("marketType");

const openingCycle =
    document.getElementById("openingCycle");

const productTags =
    document.getElementById("productTags");

const parkingStatus =
    document.getElementById("parkingStatus");

const toiletStatus =
    document.getElementById("toiletStatus");

const storeCount =
    document.getElementById("storeCount");

const establishedYear =
    document.getElementById("establishedYear");

const phoneNumber =
    document.getElementById("phoneNumber");

const homepageUrl =
    document.getElementById("homepageUrl");

const roadAddress =
    document.getElementById("roadAddress");

const lotAddress =
    document.getElementById("lotAddress");

const directionButton =
    document.getElementById("directionButton");

loadMarketDetail();

async function loadMarketDetail() {

    const marketId = getMarketId();

    if (!marketId) {
        showError("잘못된 시장 정보입니다.");
        return;
    }

    try {
        const response =
            await fetch(`/api/markets/${marketId}`);

        if (!response.ok) {
            throw new Error("시장 상세 조회에 실패했습니다.");
        }

        const market = await response.json();

        renderMarketDetail(market);

    } catch (error) {
        console.error(error);

        showError(
            "시장 정보를 불러오지 못했습니다."
        );
    }
}

function renderMarketDetail(market) {

    marketName.textContent = market.name;

    marketTypeBadge.textContent =
        market.openingCycle === "매일"
            ? "매일 열려요"
            : `${market.openingCycle} 장날`;

    marketAddress.textContent =
        market.roadAddress
        ?? market.lotAddress
        ?? "주소 정보 없음";

    marketType.textContent =
        market.marketType ?? "-";

    openingCycle.textContent =
        market.openingCycle ?? "-";

    parkingStatus.textContent =
        market.hasParking ? "있음" : "없음";

    toiletStatus.textContent =
        market.hasPublicToilet ? "있음" : "없음";

    renderProducts(market.products);

    renderOptionalInfo(market);

    roadAddress.textContent =
        market.roadAddress ?? "도로명 주소 정보 없음";

    lotAddress.textContent =
        market.lotAddress
            ? `지번 ${market.lotAddress}`
            : "";

    setupDirectionButton(market);
}

function getMarketId() {

    const params =
        new URLSearchParams(window.location.search);

    return params.get("id");
}

function renderProducts(products) {

    productTags.innerHTML = "";

    if (!products) {
        productTags.textContent =
            "취급 품목 정보가 없습니다.";
        return;
    }

    products
        .split("+")
        .forEach(product => {

            const tag =
                document.createElement("span");

            tag.className = "tag";

            tag.textContent =
                product.trim();

            productTags.appendChild(tag);
        });
}

function renderOptionalInfo(market) {

    const storeCountRow =
        document.getElementById("storeCountRow");

    const establishedYearRow =
        document.getElementById("establishedYearRow");

    const phoneRow =
        document.getElementById("phoneRow");

    const homepageRow =
        document.getElementById("homepageRow");


    // 점포 수
    if (market.storeCount != null) {
        storeCount.textContent =
            `${market.storeCount}개`;

        storeCountRow.hidden = false;

    } else {
        storeCountRow.hidden = true;
    }


    // 개설 연도
    if (market.establishedYear != null) {
        establishedYear.textContent =
            `${market.establishedYear}년`;

        establishedYearRow.hidden = false;

    } else {
        establishedYearRow.hidden = true;
    }


    // 전화번호
    if (market.phoneNumber) {

        const phone =
            market.phoneNumber.trim();

        phoneNumber.textContent = phone;

        phoneNumber.href =
            `tel:${phone}`;

        phoneRow.hidden = false;

    } else {
        phoneRow.hidden = true;
    }

    // 홈페이지
    if (market.homepageUrl) {

        let url =
            market.homepageUrl.trim();

        // www로 시작하면 http://를 붙입니다.
        if (url.startsWith("www.")) {
            url = `http://${url}`;
        }

        homepageUrl.href = url;

        homepageRow.hidden = false;

    } else {

        homepageRow.hidden = true;

        homepageUrl.removeAttribute("href");
    }
}

function setupDirectionButton(market) {

    if (
        market.latitude == null
        || market.longitude == null
    ) {
        directionButton.disabled = true;
        directionButton.textContent =
            "위치 정보 없음";

        return;
    }

    directionButton.addEventListener(
        "click",
        () => {

            console.log(
                "시장 위치:",
                market.latitude,
                market.longitude
            );

            alert(
                "길찾기 기능은 지도 연동 단계에서 연결할 예정입니다."
            );
        }
    );
}

function showError(message) {

    const marketDetail =
        document.getElementById("marketDetail");

    marketDetail.innerHTML = `
        <div class="error-message">
            ${message}
        </div>
    `;
}