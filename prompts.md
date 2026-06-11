# Donut Rush — prompts.md

백엔드 개발(Spring Boot + Kotlin) 전용 프롬프트 모음.
매 세션 시작 시 세션 시작 프롬프트를 먼저 실행하고, tasks.md 기준으로 한 태스크씩 진행한다.

---

## 세션 시작 프롬프트 (매번 복붙)

```
CLAUDE.md와 tasks.md를 먼저 읽고 알려줘:
1. 현재 완료되지 않은 백엔드 태스크 목록
2. 다음에 진행하면 좋을 태스크 추천

코드는 아직 작성하지 마.
```

---

## [BE-00] 프로젝트 초기 세팅

```
CLAUDE.md를 읽고 Donut Rush 백엔드 프로젝트를 세팅해줘.

기술 스택:
- Spring Boot 3.x + Kotlin
- MySQL
- Spring Security + Google OAuth2
- QueryDSL
- Spring Data JPA
- WebSocket (STOMP)

패키지 루트: dsm/donutrush/

패키지 구조는 헥사고날 아키텍처 + DDD 기반으로 아래를 따라줘:

전역 구조:
donutrush/
├── domain/
│   └── {도메인명}/
│       ├── domain/         # 도메인 객체
│       ├── entity/         # JPA 엔티티
│       ├── enums/          # 열거형
│       ├── exception/      # 도메인 예외
│       ├── finder/         # 복잡한 조회 로직
│       ├── mapper/         # DTO ↔ Entity 변환
│       ├── persistence/
│       │   └── repository/ # Spring Data JPA Repository
│       ├── port/
│       │   ├── in/         # UseCase 인터페이스 (입력 포트)
│       │   └── out/        # 출력 포트 인터페이스
│       ├── presentation/
│       │   └── dto/
│       │       ├── request/
│       │       └── response/
│       └── service/        # UseCase 구현체
├── global/
│   ├── annotation/
│   ├── base/               # BaseUUIDEntity, GenericMapper
│   ├── common/
│   ├── config/
│   │   ├── querydsl/
│   │   ├── redis/
│   │   ├── security/
│   │   ├── swagger/
│   │   └── web/            # CORS 등
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

아래 순서대로 세팅해줘. 각 단계 완료 후 확인받고 다음 단계로 넘어가:
1. build.gradle.kts 의존성 설정
2. application.yml (dev/prod 분리)
3. ERD 설계 — erd.md 파일에 Mermaid 다이어그램으로 작성 (User, GameResult, Badge 테이블 + 관계)
4. global 패키지 공통 코드 (BaseUUIDEntity, ErrorCode, DonutRushException, GlobalExceptionHandler)
5. Security 설정 뼈대 (permitAll 기준, 나중에 JWT 추가)
6. QueryDSL 설정
7. CORS 설정
8. 전 도메인 domain/entity/mapper 뼈대 생성
   - domain/user/ — User.kt, UserJpaEntity.kt, UserMapper.kt
   - domain/game/ — GameResult.kt, GameResultJpaEntity.kt, GameResultMapper.kt
   - domain/badge/ — Badge.kt, BadgeJpaEntity.kt, BadgeMapper.kt
   - domain/ranking/ — entity 없음, RankingResponse.kt 뼈대만
```

---

## [BE-01] Google OAuth2 + JWT 인증

```
CLAUDE.md를 참고해서 Google OAuth2 + JWT 인증을 구현해줘.

패키지 위치:
- infrastructure/googleoauth/   # Google OAuth2 클라이언트
- global/security/auth/         # 인증 처리
- global/security/jwt/          # JWT 생성/검증/필터
- domain/user/                  # 유저 도메인 (헥사고날 구조)

요구사항:
- Google OAuth2로 로그인 시 유저 정보(이메일, 이름, 프로필 이미지) 받아오기
- 신규 유저 자동 가입 처리
- Access Token + Refresh Token 발급 (JWT)
- JWT 필터로 인증 처리
- 토큰 재발급 엔드포인트

User 도메인 구조 (헥사고날):
domain/user/
├── domain/User.kt
├── entity/UserJpaEntity.kt
├── enums/
├── exception/
├── mapper/UserMapper.kt
├── persistence/repository/UserRepository.kt
├── port/
│   ├── in/
│   │   ├── GetUserUseCase.kt
│   │   └── UpdateUserUseCase.kt
│   └── out/UserPort.kt
├── presentation/
│   ├── UserController.kt
│   └── dto/
│       ├── request/UpdateUserRequest.kt
│       └── response/UserResponse.kt
└── service/UserService.kt

설계(인터페이스 + 엔티티)만 먼저 보여줘. 승인하면 구현할게.
```

---

## [BE-02] 게임 결과 저장 API

```
CLAUDE.md와 spec.md를 참고해서 POST /game/result API를 구현해줘.

패키지 위치: domain/game/ (헥사고날 구조)

domain/game/
├── domain/GameResult.kt
├── entity/GameResultJpaEntity.kt
├── enums/
│   ├── Character.kt         # 브럼프, 이정은, 부딘
│   └── Difficulty.kt        # EASY, NORMAL, HARD
├── exception/
├── mapper/GameResultMapper.kt
├── persistence/repository/GameResultRepository.kt
├── port/
│   ├── in/SaveGameResultUseCase.kt
│   └── out/GameResultPort.kt
├── presentation/
│   ├── GameController.kt
│   └── dto/
│       ├── request/SaveGameResultRequest.kt
│       └── response/SaveGameResultResponse.kt
└── service/GameService.kt

요구사항:
- spec.md의 요청/응답 스펙 그대로
- 뱃지 판정 로직:
  - "기본 클리어": success == true
  - "무실수": penaltyCount == 0
  - "스피드": clearTime < SPEED_CLEAR_THRESHOLD (상수로 뽑을 것, 임시 60_000L ms)
- 뱃지는 domain/badge/ 별도 도메인으로 분리

엔티티와 UseCase 인터페이스 먼저 보여줘.
```

---

## [BE-03] 뱃지 도메인

```
뱃지 시스템을 별도 도메인으로 구현해줘.

패키지 위치: domain/badge/ (헥사고날 구조)

domain/badge/
├── domain/Badge.kt
├── entity/BadgeJpaEntity.kt
├── enums/BadgeType.kt       # BASIC_CLEAR, NO_PENALTY, SPEED
├── exception/
├── mapper/BadgeMapper.kt
├── persistence/repository/BadgeRepository.kt
├── port/
│   ├── in/EarnBadgeUseCase.kt
│   └── out/BadgePort.kt
├── presentation/
│   └── dto/response/BadgeResponse.kt
└── service/BadgeService.kt

요구사항:
- 게임 결과 저장 시 뱃지 조건 판정 후 자동 지급
- 이미 획득한 뱃지는 중복 지급하지 않음
- 마이페이지에서 획득/미획득 뱃지 전체 조회 가능

BadgeType enum에 조건 판정 로직도 포함시켜줘 (e.g. BadgeType.isSatisfied(result: GameResult)).
```

---

## [BE-04] 랭킹 조회 API

```
CLAUDE.md와 spec.md를 참고해서 GET /ranking?type=overall|weekly API를 구현해줘.

패키지 위치: domain/ranking/ (헥사고날 구조)

domain/ranking/
├── finder/RankingFinder.kt     # QueryDSL 복잡 쿼리
├── port/
│   └── in/GetRankingUseCase.kt
├── presentation/
│   ├── RankingController.kt
│   └── dto/response/RankingResponse.kt
└── service/RankingService.kt

요구사항:
- overall: 전체 기간, clearTime ASC → createdAt DESC
- weekly: 이번 주 월요일 00:00 ~ 현재까지 기록 필터링 후 동일 정렬
- 응답: spec.md 스펙 그대로 (rank, nickname, clearTime, character, badges)
- 복잡한 조회는 반드시 finder/ 에 QueryDSL로 작성

RankingFinder 쿼리 설계 먼저 보여줘.
```

---

## [BE-05] 유저 API

```
GET /user, PUT /user API를 구현해줘.

패키지 위치: domain/user/ (BE-01에서 이미 구조 생성됨)

요구사항:
- GET /user: 현재 로그인 유저 정보 반환 (닉네임, 프로필 이미지, 플레이 통계)
  - 플레이 통계: 총 플레이 횟수, 최고 기록, 평균 클리어 시간
  - 통계 조회는 GameResult 도메인의 출력 포트를 통해 가져올 것
- PUT /user: 닉네임 변경
  - 닉네임 중복 검사 포함
  - 변경 가능 주기 제한 여부는 일단 미적용

현재 로그인 유저는 SecurityContextHolder에서 가져오는 방식으로.
```

---

## [BE-06] WebSocket 멀티플레이 — 설계

```
Donut Rush 멀티플레이 WebSocket 아키텍처를 설계해줘. 코드는 아직 작성하지 마.

스택: Spring Boot + STOMP + Kotlin

요구사항:
- 2~4인 랜덤 매칭
- 모든 플레이어 동일 난이도
- 각 플레이어의 포만감 게이지(점수) 실시간 동기화
- 누군가 90점 도달 시 승리자 전체 브로드캐스트
- 연결은 매칭 수락 시점에만 열고, 게임 종료 시 즉시 닫음

아래 항목을 설계해줘:
1. STOMP destination/topic 구조
2. 매칭 방 관리 방식 (인메모리 vs Redis)
3. 게이지 동기화 메시지 페이로드
4. 플레이어 중간 이탈 처리
5. 패키지 위치 (헥사고날 구조 기준)
```

---

## [BE-07] WebSocket 멀티플레이 — 구현

```
합의한 설계를 바탕으로 WebSocket 멀티플레이를 구현해줘.

헥사고날 구조 기준 패키지:
domain/multiplayer/
├── domain/
├── enums/
├── port/
│   ├── in/
│   └── out/
├── service/
└── presentation/  # WebSocket Controller

global/config/ 아래에 WebSocket 설정 추가.

아래 순서로 진행하고 각 단계 완료 후 확인받고 넘어가:
1. WebSocket + STOMP 설정 (global/config/)
2. 매칭 방 관리 서비스
3. 게이지 동기화 핸들러
4. 승리자 감지 및 브로드캐스트
```

---

## 디버깅 프롬프트

```
아래 에러가 발생했어:

[에러 메시지 붙여넣기]

발생 시점: [어떤 동작 중에]
관련 파일: [파일 경로]
기대 동작: [어떻게 되어야 하는지]

원인 진단하고 수정 방법 제안해줘.
관련 없는 코드는 건드리지 마.
헥사고날 아키텍처 패키지 구조 지켜줘 (CLAUDE.md 참고).
```

---

## 설계 비교 프롬프트

```
[기능]을 구현하는 두 가지 방법을 비교해줘:

방법 A: [내용]
방법 B: [내용]

우리 스택 (Spring Boot + Kotlin + MySQL + 헥사고날 아키텍처) 기준으로:
- 구현 복잡도
- 성능
- 헥사고날 아키텍처 원칙 준수 여부
- 유지보수성

추천과 이유도 알려줘.
```
