-- Flyway Migration: V1__init_schema.sql
-- Relational PostgreSQL Schema for AI Video Studio

CREATE EXTENSION IF NOT EXISTS "pgcrypto";

-- 1. Users
CREATE TABLE IF NOT EXISTS users (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    email           VARCHAR(255) NOT NULL UNIQUE,
    password_hash   VARCHAR(255) NOT NULL,
    display_name    VARCHAR(100),
    avatar_url      VARCHAR(500),
    role            VARCHAR(20) NOT NULL DEFAULT 'USER',
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_login_at   TIMESTAMP WITH TIME ZONE
);

-- 2. User API Keys
CREATE TABLE IF NOT EXISTS user_api_keys (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    provider        VARCHAR(50) NOT NULL DEFAULT 'GEMINI',
    encrypted_key   TEXT NOT NULL,
    is_active       BOOLEAN NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    last_used_at    TIMESTAMP WITH TIME ZONE,
    UNIQUE(user_id, provider)
);

-- 3. Projects
CREATE TABLE IF NOT EXISTS projects (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id         UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    title           VARCHAR(255) NOT NULL,
    description     TEXT,
    master_prompt   TEXT,
    resolution      VARCHAR(10) DEFAULT '1080p',
    aspect_ratio    VARCHAR(10) DEFAULT '16:9',
    status          VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    genre           VARCHAR(50),
    is_archived     BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 4. Project Tags
CREATE TABLE IF NOT EXISTS project_tags (
    id              UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id      UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    tag             VARCHAR(50) NOT NULL,
    UNIQUE(project_id, tag)
);

-- 5. Characters
CREATE TABLE IF NOT EXISTS characters (
    id               UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id       UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    name             VARCHAR(100) NOT NULL,
    appearance       TEXT,
    clothing         TEXT,
    personality      TEXT,
    age_range        VARCHAR(20),
    gender           VARCHAR(20),
    additional_notes TEXT,
    sort_order       INT NOT NULL DEFAULT 0,
    created_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at       TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 6. Character Reference Images
CREATE TABLE IF NOT EXISTS character_images (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    character_id        UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    r2_object_key       VARCHAR(500) NOT NULL,
    r2_url              VARCHAR(500),
    image_type          VARCHAR(20) NOT NULL DEFAULT 'UPLOADED',
    is_primary          BOOLEAN NOT NULL DEFAULT FALSE,
    original_filename   VARCHAR(100),
    file_size_bytes     BIGINT,
    mime_type           VARCHAR(50),
    width_px            INT,
    height_px           INT,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 7. Storyboards
CREATE TABLE IF NOT EXISTS storyboards (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    project_id              UUID NOT NULL REFERENCES projects(id) ON DELETE CASCADE,
    title                   VARCHAR(255) NOT NULL,
    description             TEXT,
    status                  VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    total_duration_seconds  INT DEFAULT 0,
    created_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 8. Scenes
CREATE TABLE IF NOT EXISTS scenes (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    storyboard_id       UUID NOT NULL REFERENCES storyboards(id) ON DELETE CASCADE,
    order_index         INT NOT NULL,
    prompt              TEXT NOT NULL,
    shot_type           VARCHAR(30) DEFAULT 'MEDIUM',
    camera_movement     VARCHAR(30) DEFAULT 'STATIC',
    dialogue            TEXT,
    sfx                 TEXT,
    ambient_audio       TEXT,
    duration_seconds    INT NOT NULL DEFAULT 8,
    thumbnail_r2_key    VARCHAR(500),
    thumbnail_url       VARCHAR(500),
    transition_note     TEXT,
    director_notes      TEXT,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    updated_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    UNIQUE(storyboard_id, order_index)
);

-- 9. Scene Characters (Many-to-Many Join)
CREATE TABLE IF NOT EXISTS scene_characters (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    scene_id                UUID NOT NULL REFERENCES scenes(id) ON DELETE CASCADE,
    character_id            UUID NOT NULL REFERENCES characters(id) ON DELETE CASCADE,
    scene_specific_notes    TEXT,
    UNIQUE(scene_id, character_id)
);

-- 10. Generations
CREATE TABLE IF NOT EXISTS generations (
    id                      UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    scene_id                UUID NOT NULL REFERENCES scenes(id) ON DELETE CASCADE,
    user_id                 UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    status                  VARCHAR(20) NOT NULL DEFAULT 'QUEUED',
    composed_prompt         TEXT,
    model_used              VARCHAR(30) DEFAULT 'veo-3',
    resolution              VARCHAR(10),
    duration_seconds        INT,
    attempt_number          INT NOT NULL DEFAULT 1,
    video_r2_key            VARCHAR(500),
    video_url               VARCHAR(500),
    last_frame_r2_key       VARCHAR(500),
    file_size_bytes         BIGINT,
    mime_type               VARCHAR(30) DEFAULT 'video/mp4',
    api_latency_ms          INT,
    error_message           TEXT,
    api_response_metadata   JSONB,
    queued_at               TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    started_at              TIMESTAMP WITH TIME ZONE,
    completed_at            TIMESTAMP WITH TIME ZONE
);

-- 11. Generation Assets
CREATE TABLE IF NOT EXISTS generation_assets (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    generation_id       UUID NOT NULL REFERENCES generations(id) ON DELETE CASCADE,
    asset_type          VARCHAR(30) NOT NULL,
    r2_object_key       VARCHAR(500) NOT NULL,
    r2_url              VARCHAR(500),
    file_size_bytes     BIGINT,
    mime_type           VARCHAR(30),
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW()
);

-- 12. R2 Asset Master Ledger
CREATE TABLE IF NOT EXISTS r2_assets (
    id                  UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    user_id             UUID NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    r2_object_key       VARCHAR(500) NOT NULL UNIQUE,
    asset_category      VARCHAR(30) NOT NULL,
    original_filename   VARCHAR(100),
    file_size_bytes     BIGINT NOT NULL,
    mime_type           VARCHAR(50) NOT NULL,
    reference_id        UUID,
    reference_type      VARCHAR(50),
    is_deleted          BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT NOW(),
    deleted_at          TIMESTAMP WITH TIME ZONE
);

-- Indexes for lightning fast queries
CREATE INDEX IF NOT EXISTS idx_projects_user_id ON projects(user_id);
CREATE INDEX IF NOT EXISTS idx_characters_project_id ON characters(project_id);
CREATE INDEX IF NOT EXISTS idx_storyboards_project_id ON storyboards(project_id);
CREATE INDEX IF NOT EXISTS idx_scenes_storyboard_order ON scenes(storyboard_id, order_index);
CREATE INDEX IF NOT EXISTS idx_generations_scene_id ON generations(scene_id);
CREATE INDEX IF NOT EXISTS idx_generations_user_status ON generations(user_id, status);
CREATE INDEX IF NOT EXISTS idx_generations_queued_at ON generations(queued_at DESC);
CREATE INDEX IF NOT EXISTS idx_r2_assets_user_id ON r2_assets(user_id);
CREATE INDEX IF NOT EXISTS idx_r2_assets_category ON r2_assets(user_id, asset_category);
