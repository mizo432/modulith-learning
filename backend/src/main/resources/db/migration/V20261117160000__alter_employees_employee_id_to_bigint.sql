-- employeesテーブルのemployee_idをUUIDからBIGINTへ変更
-- Issue #589: relationshipMgmtのUUIDをSnowflakeIdへ移行

-- 既存のemployeesテーブルを再作成（UUID→BIGINT）
-- 既存データは開発データの再投入で対応する前提

drop table if exists employees;

create table employees
(
    employee_id bigint       not null primary key,
    lastName    text         not null,
    firstName   text         not null,
    valid_from  timestamp    not null,
    valid_to    timestamp
);
