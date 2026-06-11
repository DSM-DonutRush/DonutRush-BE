# Donut Rush — tasks.md (백엔드 전용)

AI와 개발 시 이 파일을 기준으로 한 태스크씩 진행한다.
각 태스크 완료 후 체크박스를 체크하고 다음 태스크로 넘어간다.

---

## Phase 0: 프로젝트 세팅

- [x] build.gradle.kts 의존성 설정
- [x] application.yml (dev/prod 분리)
- [x] ERD 설계 — erd.md 파일에 Mermaid 다이어그램으로 작성
- [x] global 공통 코드 (BaseUUIDEntity, ErrorCode, DonutRushException, GlobalExceptionHandler)
- [x] Spring Security 설정 뼈대
- [x] QueryDSL 설정
- [x] CORS 설정
- [x] 전 도메인 domain/entity/mapper 뼈대 생성
  - [x] domain/user/ — User.kt, UserJpaEntity.kt, UserMapper.kt
  - [x] domain/game/ — GameResult.kt, GameResultJpaEntity.kt, GameResultMapper.kt
  - [x] domain/badge/ — Badge.kt, BadgeJpaEntity.kt, BadgeMapper.kt
  - [x] domain/ranking/ — (entity 없음, finder/response 뼈대만)

---

## Phase 1: 인증

### [BE-01] Google OAuth2 + JWT
- [x] infrastructure/googleoauth/ — Google OAuth2 클라이언트
- [x] global/security/jwt/ — JWT 생성/검증/필터
- [x] global/security/auth/ — 인증 처리
- [x] domain/user/ 전체 구조 (헥사고날)
  - [x] UserJpaEntity, User 도메인 객체
  - [x] UserRepository, UserPort
  - [x] GetUserUseCase, UpdateUserUseCase (인터페이스)
  - [x] UserService (구현체)
  - [x] UserController (GET /user, PUT /user)
- [x] 신규 유저 자동 가입 처리
- [x] Access Token + Refresh Token 발급
- [x] 토큰 재발급 엔드포인트

---

## Phase 2: 게임 핵심 API

### [BE-02] 게임 결과 저장
- [x] domain/game/ 전체 구조 (헥사고날)
  - [x] GameResultJpaEntity, GameResult 도메인 객체
  - [x] Character.kt, Difficulty.kt enum
  - [x] GameResultRepository, GameResultPort
  - [x] SaveGameResultUseCase (인터페이스)
  - [x] GameService (구현체) — 뱃지 판정 로직 포함
  - [x] GameController (POST /game/result)

### [BE-03] 뱃지 도메인
- [x] domain/badge/ 전체 구조 (헥사고날)
  - [x] BadgeJpaEntity, Badge 도메인 객체
  - [x] BadgeType.kt enum — 조건 판정 로직 포함
  - [x] BadgeRepository, BadgePort
  - [x] EarnBadgeUseCase (인터페이스)
  - [x] BadgeService (구현체) — 중복 지급 방지
- [x] 마이페이지용 전체 뱃지 조회 (획득/미획득)

### [BE-04] 랭킹 조회
- [x] domain/ranking/ 전체 구조 (헥사고날)
  - [x] RankingFinder.kt — QueryDSL (전체/주간 쿼리)
  - [x] GetRankingUseCase (인터페이스)
  - [x] RankingService (구현체)
  - [x] RankingController (GET /ranking?type=overall|weekly)

---

## Phase 3: 실시간 멀티플레이

### [BE-05] WebSocket 설계 (코드 작성 전)
- [x] STOMP destination/topic 구조 확정
- [x] 매칭 방 관리 방식 결정 (인메모리 ConcurrentHashMap)
- [x] 게이지 동기화 메시지 페이로드 확정
- [x] 이탈 처리 방식 확정 (SessionDisconnectEvent)

### [BE-06] WebSocket 구현
- [x] global/config/ — WebSocket + STOMP 설정
- [x] domain/multiplayer/ 전체 구조 (헥사고날)
  - [x] 매칭 방 관리 서비스
  - [x] 게이지 동기화 핸들러
  - [x] 승리자 감지 및 브로드캐스트
  - [x] 플레이어 이탈 처리

---

## Phase 4: 마무리

- [x] 전체 API Swagger 문서화 확인
- [x] 예외 처리 누락 점검
- [x] 통합 테스트
- [ ] 최종 배포 준비
