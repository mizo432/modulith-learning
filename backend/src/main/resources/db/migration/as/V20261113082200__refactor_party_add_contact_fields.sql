-- Partyのリファクタリング: nameフィールドを削除し、dateOfDie、contact、phoneNoを追加する

-- Personテーブルに新しいカラムを追加
alter table undecided_as.People
    add column date_of_die date,
    add column contact varchar(200),
    add column phone_no varchar(20);

-- Orgテーブルに新しいカラムを追加
alter table undecided_as.Org
    add column date_of_die date,
    add column contact varchar(200),
    add column phone_no varchar(20);

-- OrgUnitテーブルに新しいカラムを追加
alter table undecided_as.OrgUnit
    add column date_of_die date,
    add column contact varchar(200),
    add column phone_no varchar(20);

-- ContactUsesテーブルを作成
create table undecided_as.ContactUses
(
    contact_use_id bigint          not null constraint ContactUses_pk primary key,
    party_id       bigint          not null,
    contact        varchar(200),
    phone_no       varchar(20),
    address        text,
    type           varchar(20)     not null,
    when_occured   timestamp
);
