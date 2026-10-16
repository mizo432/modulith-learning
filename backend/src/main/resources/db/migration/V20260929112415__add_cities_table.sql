create schema if not exists undecided_ef;

create table undecided_ef.cities
(
    city_id        BIGINT     not null,
    prefecture_id  BIGINT     not null,
    lg_code        varchar(6) not null,
    country_name   varchar(24),
    country_kana   varchar(50),
    country_roma   varchar(100),
    city_name      varchar(24),
    city_kana      varchar(50),
    city_roma      varchar(100),
    ward_name      varchar(24),
    ward_kana      varchar(50),
    ward_roma      varchar(100),
    effective_date date       not null,
    abolition_data date       not null,
    remarks        varchar(256),
    primary key (city_id)
);

create unique index lg_code_uindex on undecided_ef.cities (lg_code);
create index idx_cities_prefecture_id on undecided_ef.cities (prefecture_id);

comment on table undecided_ef.cities is 'city table';
comment on column undecided_ef.cities.city_id is 'Unique identifier for the city';
comment on column undecided_ef.cities.prefecture_id is 'Unique identifier for the prefecture';
comment on column undecided_ef.cities.lg_code is 'Legal code';
comment on column undecided_ef.cities.country_name is 'County name';
comment on column undecided_ef.cities.country_kana is 'County kana';
comment on column undecided_ef.cities.country_roma is 'County romaji';
comment on column undecided_ef.cities.city_name is 'City name';
comment on column undecided_ef.cities.city_kana is 'City kana';
comment on column undecided_ef.cities.city_roma is 'City romaji';
comment on column undecided_ef.cities.ward_name is 'Ward name';
comment on column undecided_ef.cities.ward_kana is 'Ward kana';
comment on column undecided_ef.cities.ward_roma is 'Ward romaji';
comment on column undecided_ef.cities.effective_date is 'Effective date';
comment on column undecided_ef.cities.abolition_data is 'Abolition date';
comment on column undecided_ef.cities.remarks is 'Remarks';
