create table address_info.prefectures
(
    prefecture_id  BIGINT      not null,
    lg_code        varchar(2)  not null,
    pref_name      varchar(10) not null,
    pref_kana      varchar(50) not null,
    pref_roma      varchar(50) not null,
    effective_date date        not null,
    abolition_data date        not null,
    remarks        varchar(256),
    primary key (prefecture_id)
)