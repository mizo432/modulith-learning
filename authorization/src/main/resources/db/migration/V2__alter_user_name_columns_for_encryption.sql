-- 機微な情報暗号化対応: first_name と last_name カラムを TEXT 型に変更
-- 暗号化されたデータはBase64エンコードされた文字列となるため、VARCHAR(255)では不足する可能性がある

ALTER TABLE users
    ALTER COLUMN first_name TYPE TEXT;
ALTER TABLE users
    ALTER COLUMN last_name TYPE TEXT;
