-- Employeeエンティティ用のemployeesテーブルを作成
-- firstNameとlastNaneカラムはTEXT型で定義し、暗号化対応を前提とする

create table employees
(
    employee_id UUID       not null primary key,
    lastNane    varchar(50) not null,
    firstName   varchar(50) not null,
    valid_from  timestamp  not null,
    valid_to    timestamp
);
