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
    address        varchar(200),
    pref_name      varchar(10),
    pref_kana      varchar(15),
    postal_code    varchar(8),
    country_name   varchar(50),
    country_kana   varchar(50),
    city_name      varchar(20),
    city_kana      varchar(20),
    ward_name      varchar(20),
    ward_kana      varchar(20),
    oaza_cho_name  varchar(20),
    oaza_cho_kana  varchar(20),
    chome_name     varchar(20),
    chome_kana     varchar(20),
    koaza_name     varchar(20),
    koaza_kana     varchar(20),
    building_name  varchar(50),
    building_kana  varchar(50),
    room_no        varchar(20),
    type           varchar(20)     not null,
    when_occured   timestamp
);
