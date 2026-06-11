# Donut Rush — AGENTS.md
# (Cursor / GitHub Copilot / Codex 등 크로스 툴용 — 영어로 작성됨)

## Project Overview
Casual donut-matching game based on SDGs themes (Peace, Justice, Effective Institutions).
Players select a character (브럼프 / 이정은 / 부딘), then press arrow keys matching the displayed donuts to fill a satiety gauge.
Goal: reach 90 points (Full state) within 2 minutes.
Supports single-play and 2–4 player real-time multiplayer.

## Tech Stack
- **Frontend**: React 18, Vite, TypeScript, Phaser (game engine), Tailwind CSS
- **Backend**: Spring Boot, Kotlin, MySQL
- **Realtime**: WebSocket (STOMP) for multiplayer gauge sync

## Build & Run
```bash
# Frontend
cd frontend && npm install && npm run dev    # localhost:5173
cd frontend && npm run build
cd frontend && npm run test

# Backend
./gradlew bootRun    # localhost:8080
./gradlew test
```

## Architecture Rules
1. **Phaser ↔ React**: Use EventEmitter only. Never access React state from Phaser scenes.
2. **WebSocket lifecycle**: Open on match accept. Close immediately on game end.
3. **Assets**: `frontend/public/assets/` filenames must not be changed.
4. **API response shape**: always `{ data, message, success }`.
5. **Ranking sort**: clearTime ASC → createdAt DESC.

## Game Mechanics Summary
- 4 donut types randomly mapped to arrow keys (↑↓←→) at game start. Fixed for entire game.
- Easy: donut + arrow key hint displayed together
- Normal: donut only, player memorizes mapping
- Hard: sequence of 4–7 donuts shown (input locked) → any key to enter blind phase (black donut shown) → input sequence from memory
- Correct input: score +1
- Wrong input: 2-second input lock (gauge unchanged)
- Satiety stages: 10 / 30 / 50 / 90pts (clear)
- Time limit: 2 minutes (all difficulties)

## Domain Glossary
- `DonutMapping`: random donut-to-arrow-key mapping generated at game start; fixed throughout
- `SatietyGauge`: score-based fullness (0→90pts triggers clear)
- `Full state`: game-clear condition at 90pts
- `Sequence` (Hard mode): ordered set of 4–7 donuts shown during presentation phase
- `Blind phase` (Hard mode): donuts hidden, single black donut shown, awaiting input
- `ClearTime`: millisecond elapsed time from start to clear (used for ranking)

## API Endpoints
```
POST /game/result   → { userId, character, clearTime, success, penaltyCount, difficulty, createdAt }
GET  /ranking       → ?type=overall|weekly
GET  /user
PUT  /user
```

## Code Conventions
- TypeScript: strict mode. No `any`.
- Kotlin: ktlint. Separate domain and infrastructure packages.
- Components: PascalCase. Hooks: camelCase with `use` prefix.
- No unnecessary comments.
