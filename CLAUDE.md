# Donut Rush — CLAUDE.md

## WHAT (프로젝트 정보)

### 개요
SDGs(정의·평화·효과적인 제도) 메시지를 담은 캐주얼 도넛 매칭 게임.
사용자가 캐릭터(브럼프·이정은·부딘)를 선택하고, 화면에 등장하는 도넛에 매핑된 방향키를 눌러 포만감 게이지를 채우면 승리.
싱글 플레이와 2~4인 실시간 멀티플레이를 지원하며, 랭킹 및 뱃지 시스템으로 재참여를 유도한다.

> **이 CLAUDE.md는 백엔드(Spring Boot + Kotlin) 전용이다.**

### 기술 스택

- Spring Boot 3.x + Kotlin
- MySQL + Spring Data JPA + QueryDSL
- Spring Security + Google OAuth2 + JWT
- WebSocket (STOMP) — 실시간 멀티플레이 게이지 동기화
- Redis (캐시)

---

## WHY (설계 결정)

### 아키텍처: 헥사고날 + DDD
```
Presentation (Controller)
       ↓
   port/in (UseCase 인터페이스)
       ↓
   service (UseCase 구현체)
       ↓
   port/out (출력 포트 인터페이스)
       ↓
persistence / infrastructure (어댑터 구현체)
```

- `port/in` — UseCase 인터페이스 (입력 포트)
- `port/out` — 외부 시스템 호출 인터페이스 (출력 포트)
- `persistence` — JPA 기반 DB 어댑터 구현
- `infrastructure` — Google OAuth2, 외부 API 등 외부 어댑터

### 인증: Google OAuth2 + JWT
- Google OAuth2로 로그인 → 유저 정보(이메일, 이름, 프로필 이미지) 수신
- 신규 유저 자동 가입 처리
- Access Token + Refresh Token 발급 (JWT)
- JWT 필터로 이후 요청 인증 처리
- OAuth2 클라이언트 구현은 `infrastructure/googleoauth/`

### 복잡한 조회는 반드시 finder/ 에 QueryDSL로
랭킹, 통계처럼 여러 테이블 조인이 필요한 쿼리는 `domain/{name}/finder/`에 작성.
단순 CRUD는 Spring Data JPA Repository로 처리.

### WebSocket 연결 정책
- 매칭 수락 시점에만 연결 열기
- 게임 종료 즉시 연결 닫기

---

## HOW (패키지 구조 & 규칙)

### 전체 패키지 구조
```
dsm/donutrush/
├── domain/
│   ├── user/
│   ├── game/
│   ├── badge/
│   ├── ranking/
│   └── multiplayer/
├── global/
│   ├── annotation/
│   ├── base/               # BaseUUIDEntity, GenericMapper
│   ├── common/
│   ├── config/
│   │   ├── querydsl/
│   │   ├── redis/
│   │   ├── security/
│   │   ├── swagger/
│   │   └── web/            # CORS
│   ├── error/
│   │   ├── ErrorResponse.kt
│   │   ├── GlobalExceptionHandler.kt
│   │   └── exception/
│   │       ├── ErrorCode.kt
│   │       └── DonutRushException.kt
│   └── security/
│       ├── auth/
│       └── jwt/
└── infrastructure/
    └── googleoauth/
```

### 도메인별 표준 구조
```
domain/{name}/
├── domain/            # 도메인 객체 (비즈니스 규칙 포함)
├── entity/            # JPA 엔티티
├── enums/             # 열거형
├── exception/         # 도메인 예외
├── finder/            # 복잡한 조회 로직 (QueryDSL)
├── mapper/            # DTO ↔ Entity 변환
├── persistence/
│   └── repository/    # Spring Data JPA Repository
├── port/
│   ├── in/            # UseCase 인터페이스 (입력 포트)
│   └── out/           # 출력 포트 인터페이스
├── presentation/
│   └── dto/
│       ├── request/
│       └── response/
└── service/           # UseCase 구현체 (비즈니스 로직)
```

### 커맨드
```bash
./gradlew bootRun    # 서버 실행 (localhost:8080)
./gradlew test
./gradlew ktlintCheck
```

### 코드 컨벤션
- ktlint 기준 준수
- UseCase 인터페이스 → `port/in/`, 구현체 → `service/`
- 도메인 객체와 JPA 엔티티 반드시 분리 (domain/ vs entity/)
- API 응답: 항상 `{ data, message, success }` 구조

### 절대 건드리지 말 것
- `src/main/resources/application.yml` — 환경별 설정 분리되어 있음

---

## 도메인 용어 사전

| 용어 | 설명 |
|------|------|
| `DonutMapping` | 게임 시작 시 생성되는 도넛-방향키 1:1 매핑. 게임 내내 고정 |
| `SatietyGauge` / `포만감 게이지` | 점수 기반 게이지. 90점 도달 시 Full 상태 |
| `Full 상태` | 90점 달성으로 게임 클리어 |
| `ClearTime` | 밀리초 단위 게임 클리어 소요 시간 (랭킹 기준) |
| `브럼프 / 이정은 / 부딘` | 선택 가능한 3개의 가상 캐릭터 |
| `Badge` | 게임 클리어 조건 달성 시 지급 (기본 클리어 / 무실수 / 스피드) |

---

## 포만감 게이지 단계

| 점수 | 단계 |
|------|------|
| 10점 | 1단계 |
| 30점 | 2단계 |
| 50점 | 3단계 |
| 90점 | 클리어 |
