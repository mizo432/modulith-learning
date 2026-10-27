-- EmployeeエンティティのlastNaneフィールドをlastNameに修正したことに伴い、
-- employeesテーブルのlastNaneカラムをlastNameにリネーム

ALTER TABLE employees
    RENAME COLUMN lastNane TO lastName;
