<p align="center">
  <img
    src="./docs/images/todayjang-cover.png"
    alt="오늘장 프로젝트 소개"
    width="100%"
  />
</p>

# 오늘장

여행 지역과 날짜, 현재 위치를 기준으로  
전국 전통시장과 5일장을 쉽게 찾을 수 있는 모바일 웹 서비스입니다.

🔗 **Service**: https://todayjang.site

🚀 **Current Version**: `v1.2.0`

---

## 💡 Project

가족 여행 중 5일장 정보를 찾기 어려워
도로의 현수막을 통해 장날을 확인했던 경험에서 시작했습니다.

공공데이터를 활용해 여행 중 필요한 장날 정보를
더 빠르고 직관적으로 확인할 수 있도록 구현했습니다.

---

## ✨ Features

- 여행 지역과 방문 날짜를 기준으로 그날 열리는 5일장·상설시장 검색
- 현재 위치에서 20km 이내에 오늘 열리는 5일장 검색
- 검색 결과를 목록과 지도 방식으로 전환
- 지도 이동 및 확대·축소, 시장 위치 마커 표시
- 시장 유형, 장날, 점포 수, 주소, 취급 품목 및 편의시설 정보 제공

---

## 🤖 AI 활용

ChatGPT를 활용해 서비스 아이디어를 구체화하고,
사용자 흐름과 UI 디자인을 기획했습니다.

Codex CLI와 협업해 코드 구현, 리팩터링, 디버깅,
테스트와 동작 검증, 문서화를 진행했습니다.

AI가 제안한 결과는 서비스 요구사항과 실제 실행 환경을 기준으로
직접 확인하고 수정하며 프로젝트에 반영했습니다.

---

## 🛠 Tech Stack

| 종류 | 사용 기술 |
| --- | --- |
| ☕️ Backend | Java, Spring Boot, Spring Data JPA, REST API |
| 🗄️ Database | PostgreSQL |
| 🎨 Frontend | HTML, CSS, JavaScript, Leaflet, OpenStreetMap |
| ☁️ Infra | Docker, Docker Compose, AWS EC2, Nginx, HTTPS |

---

## 🏗 Architecture

```text
Client
  │
  │ HTTPS
  ▼
Nginx
  │
  ▼
Spring Boot
  │
  ▼
PostgreSQL
```
