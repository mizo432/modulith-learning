create table prefectures
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

create unique index prefecture_code_uindex on prefectures (prefecture_code);

comment on table prefectures is 'prefecture table';
comment on column prefectures.prefecture_id is 'Unique identifier for the prefecture';
comment on column prefectures.prefecture_code is 'Prefecture code';
comment on column prefectures.lg_code is 'Legal code';
comment on column prefectures.pref_name is 'Prefecture name';
comment on column prefectures.pref_kana is 'Prefecture kana';
comment on column prefectures.pref_roma is 'Prefecture romaji';
comment on column prefectures.effective_date is 'Effective date';
comment on column prefectures.abolition_data is 'Abolition date';
comment on column prefectures.remarks is 'Remarks';
