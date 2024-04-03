CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

-- DROP TABLE solutions CASCADE;
-- DROP TABLE validations CASCADE;
-- DROP TABLE challenges CASCADE;
-- DROP TABLE users CASCADE;

CREATE TABLE IF NOT EXISTS users (
    "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    "name" VARCHAR(80) NOT NULL,
    surname VARCHAR(80) NOT NULL,
    "email" VARCHAR(255) NOT NULL,
    "password" VARCHAR(64) NOT NULL,
    avatar TEXT,
    craeted_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
    deleted_at DATE
);

CREATE UNIQUE INDEX IF NOT EXISTS email_idx ON users (email);

CREATE TABLE IF NOT EXISTS challenges (
    "id" UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    title VARCHAR(255) NOT NULL,
    "description" TEXT NOT NULL,
    creator_id UUID NOT NULL,
    craeted_at DATE NOT NULL DEFAULT CURRENT_DATE,
    updated_at DATE NOT NULL DEFAULT CURRENT_DATE,
    deleted_at DATE,

    FOREIGN KEY("creator_id") REFERENCES users ON DELETE CASCADE
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
    "language" VARCHAR(20) NOT NULL,
    challenge_id UUID NOT NULL,
    submitter_id UUID NOT NULL,
    PRIMARY KEY("version", "challenge_id", "submitter_id"),
    FOREIGN KEY("challenge_id") REFERENCES challenges ON DELETE CASCADE,
    FOREIGN KEY("submitter_id") REFERENCES users ON DELETE CASCADE
);


