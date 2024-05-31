create table "address_info"."prefectures"
(
    prefecture_id numeric(19) not null,
    lg_code       varchar(2)  not null,
    pref_name     varchar(10) not null,
    pref_kana     varchar(50) not null,
    pref_roma     varchar(50) not null,
    efct_date     date,
    ablt_date     date,
    remarks       varchar(256),
    primary key (prefecture_id)
)