<h1>
  <img src="./src/main/resources/static/images/logo.png" width="32" />
  오늘장 - 전국 5일장 정보 서비스
</h1>

여행 지역과 날짜, 현재 위치를 기준으로  
전국 전통시장과 5일장을 쉽게 찾을 수 있는 웹 서비스입니다.

🔗 Service: https://todayjang.site

---

## 📌 Overview

가족 여행 중 지역의 5일장을 방문하려 했지만,
온라인에서 장날 정보를 찾기 어려워 도로의 현수막을 통해 장날 정보를 확인했었던 경험에서 시작한 프로젝트입니다.

기존 5일장 정보 서비스는 모바일 환경에서 사용하기 불편하거나,
여행 중 필요한 장날 정보를 빠르게 확인하기 어렵다는 점에 주목했습니다.

오늘장은 공공데이터를 기반으로 전국 전통시장과 5일장 정보를 제공하고,
사용자가 선택한 지역과 날짜에 실제로 열리는 시장을 쉽게 확인할 수 있도록 구성했습니다.

또한 현재 위치를 기준으로 가까운 시장을 확인할 수 있습니다.

---

## 🤖 AI 활용

기획, 데이터 분석, 구현 방향 검토, 디버깅, 문서화 등
개발 전 과정에서 ChatGPT를 보조 도구로 적극 활용했습니다.

AI의 답변을 그대로 적용하기보다 직접 검증하고 수정하는 방식으로 활용했으며,
이를 통해 아이디어를 빠르게 구체화하고 개발 과정의 반복 작업을 줄였습니다.

기획부터 구현, 배포까지 약 3일 동안 집중적으로 개발하여
하나의 아이디어를 실제 사용 가능한 서비스로 완성했습니다.

---

## 🛠 Tech Stack

### ☕ Backend
- Java
- Spring Boot
- Spring Data JPA
- REST API

### 🗄 Database
- PostgreSQL

### 🎨 Frontend
- HTML
- CSS
- JavaScript

### ☁️ Infra
- Docker
- Docker Compose
- AWS EC2
- Nginx
- HTTPS

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
