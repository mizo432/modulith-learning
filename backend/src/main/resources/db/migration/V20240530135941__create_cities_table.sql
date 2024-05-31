create table "address_info"."cities"
(
    city_id       numeric(19, 0) not null,
    prefecture_id numeric(19, 0) not null,
    lg_code       varchar(6)     not null,
    county_name   varchar(24),
    county_kana   varchar(50),
    county_roma   varchar(100),
    city_name     varchar(24),
    city_kana     varchar(50),
    city_roma     varchar(100),
    ward_name     varchar(24),
    ward_kana     varchar(50),
    ward_roma     varchar(100),
    efct_date     date,
    ablt_date     date,
    remarks       varchar(256),
    primary key (city_id)
)