# 📅 Kari-Calendar (카리 캘린더)
> **중복 없는 일정 참여와 효율적인 스케줄 관리를 위한 스마트 캘린더 웹 서비스** > 스프링 시큐리티 기반의 탄탄한 인증 체계와 쫀득한 UI 디테일을 자랑하는 웹 애플리케이션입니다. ✨

---

## 🛠 Tech Stack

| Category | Technology | Detail |
| :--- |:------------------| :--- |
| **Backend** | **Spring Boot 4.0.5** | Java 17, Spring Security |
| **Database** | **MySQL 8.0** | JPA / Hibernate (DDL Auto) |
| **Frontend** | **Thymeleaf** | HTML5, CSS3, JavaScript (Vanilla) |
| **Build Tool** | **Gradle 8.x** | Dependency Management |

---

## ✨ Key Features

### 🔐 1. 탄탄한 회원 관리 & 인증 시스템 (Spring Security)
- **세션 기반 인증:** `@AuthenticationPrincipal`을 활용한 안전한 세션 유저 정보 관리 및 마이페이지(`profile`) 연동.
- **철저한 로그아웃 방어:** CSRF 토큰 체계를 무너뜨리지 않는 **정석적인 `POST` 방식 로그아웃** 프로세스 구축.
- **디테일한 UI/UX 피드백:** - 로그인 실패 시 주소창 파라미터(`?error`)를 감지해 동적 에러 메시지 팝업.
  - 로그아웃 성공 시 안전하게 세션과 쿠키를 무효화하고 안내 메시지(`?logout`) 동적 표출.

### 📅 2. 스마트 일정 관리 & 방장/참여 권한 구분
- **권한별 화면 바인딩:** 메인 대시보드 내에서 내가 개설한 일정(**방장**)과 초대받은 일정(**참여**)을 직관적으로 분리하여 렌더링.
- **초대 시스템:** 각 일정별 고유 `Share Key`를 통한 유연한 사용자 초대 및 공유 레이아웃 구축.
- **컬러 중복 방지:** 일정 참여 시 기존 참여자가 선택한 색상을 실시간으로 필터링하여 시각적 혼선 차단.

### 🎨 3. 감성적이고 모던한 UI 디테일
- 마우스 오버 시 부드럽게 위로 정렬되는 `transition` 기반 인터랙티브 디자인 카드 CSS 반영.
- 버튼 active 상태의 정밀한 스케일 조정을 통해 엔터키/클릭 시 쫀득한 손맛(?)을 제공하는 입력 폼 마감.

---

## 📂 Project Structure
```text
kari-calendar/
├── src/
│   ├── main/
│   │   ├── java/com/kari/karicalender/
│   │   │   ├── config/        # Security 환경 설정 (로그인/로그아웃 핸들러, CSRF)
│   │   │   ├── controller/    # 화면 매핑 컨트롤러 & REST API 컨트롤러 분리 설계
│   │   │   ├── domain/        # Entity 객체 및 계층간 데이터 전송을 위한 DTO
│   │   │   ├── repository/    # Spring Data JPA Repository 인터페이스
│   │   │   └── service/       # 비즈니스 로직 핵심 레이어 (회원가입 중복 예외 처리 등)
│   │   └── resources/
│   │       ├── static/        # 모던 인터랙션 CSS 및 정적 리소스
│   │       └── templates/     # Thymeleaf 기반 동적 HTML 뷰 템플릿
└── build.gradle               # 빌드 및 의존성 라이브러리 설정
