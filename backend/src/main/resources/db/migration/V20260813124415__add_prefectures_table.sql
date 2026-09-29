create schema if not exists address_reg;

create table address_reg.prefectures
(
    prefecture_id   BIGINT      not null,
    prefecture_code varchar(2)  not null,
    lg_code         varchar(6)  not null,
    pref_name       varchar(10) not null,
    pref_kana       varchar(50) not null,
    pref_roma       varchar(50) not null,
    effective_date  date        not null,
    abolition_data  date        not null,
    remarks         varchar(256),
    primary key (prefecture_id)
);

create unique index prefecture_code_uindex on address_reg.prefectures (prefecture_code);

comment on table address_reg.prefectures is 'prefecture table';
comment on column address_reg.prefectures.prefecture_id is 'Unique identifier for the prefecture';
comment on column address_reg.prefectures.prefecture_code is 'Prefecture code';
comment on column address_reg.prefectures.lg_code is 'Legal code';
comment on column address_reg.prefectures.pref_name is 'Prefecture name';
comment on column address_reg.prefectures.pref_kana is 'Prefecture kana';
comment on column address_reg.prefectures.pref_roma is 'Prefecture romaji';
comment on column address_reg.prefectures.effective_date is 'Effective date';
comment on column address_reg.prefectures.abolition_data is 'Abolition date';
comment on column address_reg.prefectures.remarks is 'Remarks';
