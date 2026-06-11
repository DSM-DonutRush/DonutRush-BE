CREATE TABLE IF NOT EXISTS users (
    id BINARY(16) NOT NULL,
    email VARCHAR(255) NOT NULL,
    nickname VARCHAR(255) NOT NULL,
    profile_image VARCHAR(255),
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_users PRIMARY KEY (id),
    CONSTRAINT uk_users_email UNIQUE (email),
    CONSTRAINT uk_users_nickname UNIQUE (nickname)
);

CREATE TABLE IF NOT EXISTS game_results (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    `character` VARCHAR(255) NOT NULL,
    clear_time BIGINT NOT NULL,
    success BOOLEAN NOT NULL,
    penalty_count INT NOT NULL,
    difficulty VARCHAR(255) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_game_results PRIMARY KEY (id),
    CONSTRAINT fk_game_results_user FOREIGN KEY (user_id) REFERENCES users (id)
);

CREATE INDEX idx_game_results_user_id ON game_results (user_id);
CREATE INDEX idx_game_results_ranking ON game_results (success, clear_time, created_at);

CREATE TABLE IF NOT EXISTS badges (
    id BINARY(16) NOT NULL,
    user_id BINARY(16) NOT NULL,
    badge_type VARCHAR(255) NOT NULL,
    earned_at DATETIME(6) NOT NULL,
    CONSTRAINT pk_badges PRIMARY KEY (id),
    CONSTRAINT fk_badges_user FOREIGN KEY (user_id) REFERENCES users (id),
    CONSTRAINT uk_badges_user_badge_type UNIQUE (user_id, badge_type)
);

CREATE INDEX idx_badges_user_id ON badges (user_id);
