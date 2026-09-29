create schema if not exists address_reg;

create table address_reg.chou_aza
(
    chou_aza_id        BIGINT     not null,
    city_id            BIGINT     not null,
    lg_code            varchar(6) not null,
    machiaza_code      varchar(7) not null,
    machiaza_type      varchar(1) not null,
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
    rsdt_addr_flg      boolean    not null,
    rsdt_addr_mtd_code varchar(1),
    oaza_cho_aka_flg   boolean    not null,
    koaza_aka_code     varchar(1),
    oaza_cho_gsi_uncmn varchar(50),
    koaza_gsi_uncmn    varchar(50),
    status             smallint   not null,
    wake_num_flg       boolean    not null,
    src_code           varchar(2),
    effective_date     date       not null,
    abolition_data     date       not null,
    remarks            varchar(256),
    primary key (chou_aza_id)
);

create unique index uidx_chou_aza_lg_machiaza on address_reg.chou_aza (lg_code, machiaza_code);
create index idx_chou_aza_city_id on address_reg.chou_aza (city_id);

comment on table address_reg.chou_aza is 'chou_aza table';
comment on column address_reg.chou_aza.chou_aza_id is 'Unique identifier for the chou aza';
comment on column address_reg.chou_aza.city_id is 'Unique identifier for the city';
comment on column address_reg.chou_aza.lg_code is 'Legal code';
comment on column address_reg.chou_aza.machiaza_code is 'Machiaza code';
comment on column address_reg.chou_aza.machiaza_type is 'Machiaza type code';
comment on column address_reg.chou_aza.oaza_cho_name is 'Oaza cho name';
comment on column address_reg.chou_aza.oaza_cho_kana is 'Oaza cho kana';
comment on column address_reg.chou_aza.oaza_cho_roma is 'Oaza cho romaji';
comment on column address_reg.chou_aza.chome_name is 'Chome name';
comment on column address_reg.chou_aza.chome_kana is 'Chome kana';
comment on column address_reg.chou_aza.chome_number is 'Chome number';
comment on column address_reg.chou_aza.koaza_name is 'Koaza name';
comment on column address_reg.chou_aza.koaza_kana is 'Koaza kana';
comment on column address_reg.chou_aza.koaza_roma is 'Koaza romaji';
comment on column address_reg.chou_aza.machiaza_dist is 'Machiaza distinction code';
comment on column address_reg.chou_aza.rsdt_addr_flg is 'Residential address flag';
comment on column address_reg.chou_aza.rsdt_addr_mtd_code is 'Residential address method code';
comment on column address_reg.chou_aza.oaza_cho_aka_flg is 'Oaza cho alias flag';
comment on column address_reg.chou_aza.koaza_aka_code is 'Koaza alias code';
comment on column address_reg.chou_aza.oaza_cho_gsi_uncmn is 'Oaza cho GSI uncommon characters';
comment on column address_reg.chou_aza.koaza_gsi_uncmn is 'Koaza GSI uncommon characters';
comment on column address_reg.chou_aza.status is 'Status flag';
comment on column address_reg.chou_aza.wake_num_flg is 'Wake num flag';
comment on column address_reg.chou_aza.src_code is 'Source code';
comment on column address_reg.chou_aza.effective_date is 'Effective date';
comment on column address_reg.chou_aza.abolition_data is 'Abolition date';
comment on column address_reg.chou_aza.remarks is 'Remarks';
