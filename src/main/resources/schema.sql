-- ==========================================
-- 2. Core Tables
-- ==========================================
CREATE TABLE IF NOT EXISTS users (
                                     id SERIAL PRIMARY KEY,
                                     first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    username VARCHAR(50) UNIQUE NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    password_hash VARCHAR(255) NOT NULL,

    -- Enforce the constraints directly on the columns
    gender VARCHAR(20) CHECK (gender IN ('MALE', 'FEMALE')),
    sexual_preference VARCHAR(20) DEFAULT 'BISEXUAL' CHECK (sexual_preference IN ('HETEROSEXUAL', 'HOMOSEXUAL', 'BISEXUAL')),

    birthday TIMESTAMP,
    phone_number VARCHAR(20),
    bio TEXT,
    rating INTEGER DEFAULT 0,
    last_connection TIMESTAMP,
    is_verified BOOLEAN DEFAULT false,
    verification_token VARCHAR(255),
    reset_token VARCHAR(255)
    );

CREATE TABLE IF NOT EXISTS interests (
                                         id SERIAL PRIMARY KEY,
                                         name VARCHAR(50) UNIQUE NOT NULL,
    category VARCHAR(50)
    );

-- ==========================================
-- 3. Relationships & Data
-- ==========================================
CREATE TABLE IF NOT EXISTS user_interests (
                                              user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    interest_id INTEGER REFERENCES interests(id) ON DELETE CASCADE,
    PRIMARY KEY (user_id, interest_id)
    );

CREATE TABLE IF NOT EXISTS user_photos (
                                           id SERIAL PRIMARY KEY,
                                           user_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    file_path VARCHAR(255) NOT NULL,
    is_profile_picture BOOLEAN DEFAULT false
    );

CREATE TABLE IF NOT EXISTS user_location (
                                             user_id INTEGER PRIMARY KEY REFERENCES users(id) ON DELETE CASCADE,
    latitude DECIMAL(9,6),
    longitude DECIMAL(9,6),
    city_name VARCHAR(100)
    );

-- ==========================================
-- 4. User Interactions (Self-Joins)
-- ==========================================
CREATE TABLE IF NOT EXISTS likes (
                                     id SERIAL PRIMARY KEY,
                                     liker_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    liked_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (liker_id, liked_id)
    );

CREATE TABLE IF NOT EXISTS visits (
                                      id SERIAL PRIMARY KEY,
                                      visitor_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    visited_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
    );

CREATE TABLE IF NOT EXISTS blocks (
                                      id SERIAL PRIMARY KEY,
                                      blocker_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    blocked_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (blocker_id, blocked_id)
    );

CREATE TABLE IF NOT EXISTS reports (
                                       id SERIAL PRIMARY KEY,
                                       reporter_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    reported_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE (reporter_id, reported_id)
    );

-- ==========================================
-- 5. Real-Time Engine (Chat & Notifications)
-- ==========================================
CREATE TABLE IF NOT EXISTS messages (
                                        id SERIAL PRIMARY KEY,
                                        sender_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    content TEXT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT false
    );

CREATE TABLE IF NOT EXISTS notifications (
                                             id SERIAL PRIMARY KEY,
                                             sender_id INTEGER REFERENCES users(id) ON DELETE CASCADE,
    receiver_id INTEGER REFERENCES users(id) ON DELETE CASCADE,

    -- Constraint instead of Enum
    type VARCHAR(20) NOT NULL CHECK (type IN ('LIKE', 'VIEW', 'MESSAGE', 'MATCH', 'UNLIKE')),

    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    is_read BOOLEAN DEFAULT false
    );