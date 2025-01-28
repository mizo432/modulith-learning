create table "address_info"."postal_code_map"
(
    postal_code_map_id BIGINT     not null,
    machi_aza_id       BIGINT     not null,
    postal_code        varchar(7) not null,
    effective_data     date       not null,
    abolition_data     date       not null,
    primary key (postal_code_map_id)
);

create unique index uk1_postal_code_map
    on address_info.postal_code_map
        (
         machi_aza_id,
         postal_code,
         effective_data
            );

create index ix1_postal_code_map
    on address_info.postal_code_map
        (
         postal_code,
         effective_data,
         abolition_data
            );

comment on table address_info.postal_code_map is 'This table stores the mapping of postal codes to machi_aza_id';
comment on column address_info.postal_code_map.postal_code_map_id is 'Unique identifier for the postal code map';
comment on column address_info.postal_code_map.machi_aza_id is 'Unique identifier for the machi_aza';
comment on column address_info.postal_code_map.postal_code is 'Postal code';
comment on column address_info.postal_code_map.effective_data is 'Effective date of the postal code mapping';
comment on column address_info.postal_code_map.abolition_data is 'Abolition date of the postal code mapping';
