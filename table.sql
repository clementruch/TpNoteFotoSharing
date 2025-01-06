-- Create the database
CREATE DATABASE IF NOT EXISTS FotoSharing;
USE FotoSharing;

-- Table for users
CREATE TABLE IF NOT EXISTS user (
                                    id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                    username VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL, -- Updated column name from 'password_hash' to 'password'
    role ENUM('USER', 'ADMIN', 'MODERATOR', 'VISITOR') DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
    );

-- Table for photos
CREATE TABLE IF NOT EXISTS photo (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     title VARCHAR(255) NOT NULL,
    description TEXT,
    url VARCHAR(255) NOT NULL,
    visibility ENUM('PRIVATE', 'PUBLIC') DEFAULT 'PRIVATE',
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES user(id) ON DELETE CASCADE -- Updated FK reference from 'utilisateur' to 'user'
    );

-- Table for albums
CREATE TABLE IF NOT EXISTS album (
                                     id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                     name VARCHAR(255) NOT NULL,
    description TEXT,
    visibility ENUM('PRIVATE', 'PUBLIC') DEFAULT 'PRIVATE',
    owner_id BIGINT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES user(id) ON DELETE CASCADE -- Updated FK reference from 'utilisateur' to 'user'
    );

-- Table for photos within albums
CREATE TABLE IF NOT EXISTS album_photo (
                                           album_id BIGINT NOT NULL,
                                           photo_id BIGINT NOT NULL,
                                           PRIMARY KEY (album_id, photo_id),
    FOREIGN KEY (album_id) REFERENCES album(id) ON DELETE CASCADE,
    FOREIGN KEY (photo_id) REFERENCES photo(id) ON DELETE CASCADE
    );

-- Table for comments
CREATE TABLE IF NOT EXISTS commentaire (
                                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                           text TEXT NOT NULL,
                                           photo_id BIGINT NOT NULL,
                                           author_id BIGINT NOT NULL,
                                           created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                           updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                                           FOREIGN KEY (photo_id) REFERENCES photo(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES user(id) ON DELETE CASCADE -- Updated FK reference from 'utilisateur' to 'user'
    );

-- Table for photo sharing permissions
CREATE TABLE IF NOT EXISTS partage (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       photo_id BIGINT NOT NULL,
                                       user_id BIGINT NOT NULL,
                                       permission_level ENUM('VIEW', 'EDIT', 'ADMIN') DEFAULT 'VIEW',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (photo_id) REFERENCES photo(id) ON DELETE CASCADE,
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE -- Updated FK reference from 'utilisateur' to 'user'
    );

-- Table for contacts
CREATE TABLE IF NOT EXISTS contact (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       user_id BIGINT NOT NULL,
                                       contact_id BIGINT NOT NULL,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                       selected TINYINT(1) DEFAULT 0, -- Added the selected column with a default value
    status ENUM('ACCEPTED', 'DECLINED', 'PENDING') DEFAULT 'PENDING', -- Added the status column
    FOREIGN KEY (user_id) REFERENCES user(id) ON DELETE CASCADE,
    FOREIGN KEY (contact_id) REFERENCES user(id) ON DELETE CASCADE,
    UNIQUE (user_id, contact_id) -- Évite les doublons
    );

-- Indexes to optimize queries
CREATE INDEX idx_photo_owner ON photo(owner_id);
CREATE INDEX idx_album_owner ON album(owner_id);
CREATE INDEX idx_partage_user ON partage(user_id);
