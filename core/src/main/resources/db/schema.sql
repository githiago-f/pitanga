CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- DROP TABLE solutions CASCADE;
-- DROP TABLE validations CASCADE;
-- DROP TABLE challenges CASCADE;

CREATE TABLE IF NOT EXISTS challenges (
    "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title TEXT NOT NULL,
    "description" TEXT NOT NULL,
    "level" TEXT NOT NULL,
    "base_code" TEXT DEFAULT 'public class Main {\n\tpublic static void main(String[] args) {\n\t\t// Solução\n\t}\n}',
    creator_id TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    deleted_at TIMESTAMP
);

CREATE SEQUENCE IF NOT EXISTS validations_id_seq;

CREATE TABLE IF NOT EXISTS validations (
    "id" BIGINT NOT NULL DEFAULT nextval('validations_id_seq'),
    challenge_id UUID NOT NULL,
    test_input TEXT,
    expected_output TEXT,
    PRIMARY KEY("id", "challenge_id"),
    FOREIGN KEY("challenge_id") REFERENCES challenges ON DELETE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS solutions_version_seq;

CREATE TABLE IF NOT EXISTS solutions (
    "version" BIGINT NOT NULL DEFAULT nextval('solutions_version_seq'),
    "code" TEXT NOT NULL,
    "hash" TEXT NOT NULL,
    "language" TEXT NOT NULL,
    pass_all_validations BOOLEAN DEFAULT false,
    challenge_id UUID NOT NULL,
    submitter_id TEXT NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    PRIMARY KEY("version", "challenge_id", "submitter_id"),
    FOREIGN KEY("challenge_id") REFERENCES challenges ON DELETE CASCADE
);

CREATE SEQUENCE IF NOT EXISTS langauges_id_seq;

CREATE TABLE IF NOT EXISTS languages (
    "id" BIGINT NOT NULL DEFAULT nextval('langauges_id_seq'),
    "name" TEXT NOT NULL,
    "source_file" TEXT NOT NULL,
    "compile_cmd" TEXT NOT NULL,
    "run_command" TEXT NOT NULL,
    PRIMARY KEY("id")
);
