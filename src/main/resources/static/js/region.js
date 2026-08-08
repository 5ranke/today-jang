// 도/광역시 목록 조회
// 시·군·구 목록 조회

export async function loadProvinces() {
    const response = await fetch("/api/regions/provinces");

    if (!response.ok) {
        throw new Error("지역 목록 조회에 실패했습니다.");
    }

    return response.json();
}

export async function loadCities(province) {
    const response = await fetch(
        `/api/regions/cities?province=${encodeURIComponent(province)}`
    );

    if (!response.ok) {
        throw new Error("시·군·구 목록 조회에 실패했습니다.");
    }

    return response.json();
}