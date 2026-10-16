create schema if not exists undecided_ef;

create table undecided_ef.prefectures
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

create unique index prefecture_code_uindex on undecided_ef.prefectures (prefecture_code);

comment on table undecided_ef.prefectures is 'prefecture table';
comment on column undecided_ef.prefectures.prefecture_id is 'Unique identifier for the prefecture';
comment on column undecided_ef.prefectures.prefecture_code is 'Prefecture code';
comment on column undecided_ef.prefectures.lg_code is 'Legal code';
comment on column undecided_ef.prefectures.pref_name is 'Prefecture name';
comment on column undecided_ef.prefectures.pref_kana is 'Prefecture kana';
comment on column undecided_ef.prefectures.pref_roma is 'Prefecture romaji';
comment on column undecided_ef.prefectures.effective_date is 'Effective date';
comment on column undecided_ef.prefectures.abolition_data is 'Abolition date';
comment on column undecided_ef.prefectures.remarks is 'Remarks';
