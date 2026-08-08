<h1>
  <img src="./src/main/resources/static/images/logo.png" width="32" />
  오늘장 - 전국 5일장 정보 서비스
</h1>

여행 지역과 날짜, 현재 위치를 기준으로  
전국 전통시장과 5일장을 쉽게 찾을 수 있는 웹 서비스입니다.

🔗 Service: https://todayjang.site

---

## 📌 Overview

여행 중 전통시장이나 5일장을 방문하고 싶어도  
지역별 장날을 직접 검색하고 날짜를 계산해야 하는 불편함이 있습니다.

오늘장은 공공데이터를 기반으로 시장 정보를 제공하고,
사용자가 선택한 날짜에 실제로 열리는 시장을 계산하여 보여줍니다.

또한 현재 위치를 기준으로 가까운 시장을 확인할 수 있습니다.

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
