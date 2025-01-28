create table address_info.cities
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
)