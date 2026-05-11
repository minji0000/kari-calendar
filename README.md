# 📅 Kari-Calendar (카리 캘린더)
> **중복 없는 일정 참여와 효율적인 스케줄 관리를 위한 스마트 캘린더 웹 서비스**

---

## 🛠 Tech Stack

| Category | Technology | Detail |
| :--- | :--- | :--- |
| **Backend** | Spring Boot 3.x | Java 17, Spring Security |
| **Database** | Oracle | JPA / Hibernate |
| **Frontend** | Thymeleaf | HTML5, CSS3, JavaScript |
| **Build Tool** | Gradle | Dependency Management |

---

## ✨ Key Features
- **일정 관리:** 개인 및 공유 일정의 생성, 수정, 삭제 기능.
- **초대 시스템:** 고유 Share Key를 활용한 사용자 초대 기능.
- **컬러 중복 방지:** 일정 참여 시 기존 참여자가 선택한 색상을 실시간으로 제외하여 시각적 중복 방지.
- **보안 강화:** Spring Security 기반 인증 및 CSRF 방어 체계 구축.

---

## 📂 Project Structure
```text
kari-calendar/
├── src/
│   ├── main/
│   │   ├── java/com/kari/karicalender/
│   │   │   ├── config/        # Security, Web 설정
│   │   │   ├── controller/    # 화면 매핑 및 API
│   │   │   ├── domain/        # Entity 및 DTO
│   │   │   ├── repository/    # JPA Repository 인터페이스
│   │   │   └── service/       # 핵심 비즈니스 로직
│   │   └── resources/
│   │       ├── static/        # CSS, JS, Images
│   │       └── templates/     # Thymeleaf HTML 파일
└── build.gradle               # 빌드 및 라이브러리 설정