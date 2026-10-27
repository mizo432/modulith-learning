-- 機微な情報暗号化対応: employees テーブルの firstName と lastNane カラムを TEXT 型に変更
-- 暗号化されたデータはBase64エンコードされた文字列となるため、VARCHARでは不足する可能性がある

ALTER TABLE employees
    ALTER COLUMN firstName TYPE TEXT;
ALTER TABLE employees
    ALTER COLUMN lastNane TYPE TEXT;
