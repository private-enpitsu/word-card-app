-- 1. テーブルの作成
-- en_quiz 用スキーマ（テーブル作成）
-- 既に存在する場合は作成しません（安全側）

CREATE TABLE IF NOT EXISTS `admin_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '管理者ID',
  `login_id` VARCHAR(100) NOT NULL COMMENT 'ログインID（管理者用）',
  `password` VARCHAR(255) NOT NULL COMMENT 'パスワード（将来ハッシュを格納）',
  `name` VARCHAR(100) NOT NULL COMMENT '表示名',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_admin_login_id` (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='管理者ユーザー';

CREATE TABLE IF NOT EXISTS `user_account` (
  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'ユーザーID',
  `login_id` VARCHAR(100) NOT NULL COMMENT 'ログインID（メールアドレス等を想定）',
  `password` VARCHAR(255) NOT NULL COMMENT 'パスワード（将来ハッシュを格納）',
  `name` VARCHAR(100) NOT NULL COMMENT '表示名',
  `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '作成日時',
  `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新日時',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uq_user_login_id` (`login_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci COMMENT='アプリ利用ユーザー';

CREATE TABLE IF NOT EXISTS `word` (
  `id` BIGINT NOT NULL AUTO_INCREMENT,
  `english` VARCHAR(200) NOT NULL,
  `japanese` VARCHAR(200) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;