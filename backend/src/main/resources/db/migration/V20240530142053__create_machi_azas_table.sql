create table "address_info"."machi_azas"
(
    machi_aza_id       numeric(19, 0) not null,
    city_id            numeric(19, 0) not null,
    lg_code            varchar(6)     not null,
    machiaza_code      varchar(7)     not null,
    machiaza_type      varchar(1)     not null,
    oaza_cho_name      varchar(120),
    oaza_cho_kana      varchar(240),
    oaza_cho_roma      varchar(180),
    chome_name         varchar(32),
    chome_kana         varchar(50),
    chome_number       varchar(2),
    koaza_name         varchar(120),
    koaza_kana         varchar(240),
    koaza_roma         varchar(180),
    machiaza_dist      varchar(120),
    rsdt_addr_flg      bit            not null,
    rsdt_addr_mtd_code varchar(1),
    oaza_cho_aka_flg   bit            not null,
    koaza_aka_code     varchar(1),
    oaza_cho_gsi_uncmn varchar(50),
    koaza_gsi_uncmn    varchar(50),
    status_flg         bit            not null,
    wake_num_flg       bit            not null,
    src_code           varchar(2),
    efct_date          date,
    ablt_date          date,
    remarks            varchar(256),
    primary key (machi_aza_id)
)