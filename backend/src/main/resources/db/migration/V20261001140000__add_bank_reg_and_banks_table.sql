create schema if not exists bank_reg;

create table bank_reg.banks
(
    bank_code          varchar(4)   not null,
    bank_name          varchar(128) not null,
    bank_half_kana     varchar(256),
    bank_full_kana     varchar(256),
    bank_full_hira     varchar(256),
    business_type_code varchar(8),
    business_type      varchar(64),
    dataset_id         varchar(128) not null,
    primary key (bank_code)
);

create index idx_banks_dataset_id on bank_reg.banks (dataset_id);

comment on table bank_reg.banks is 'bank table';
comment on column bank_reg.banks.bank_code is 'Bank code (4 digits)';
comment on column bank_reg.banks.bank_name is 'Bank name';
comment on column bank_reg.banks.bank_half_kana is 'Bank name in half-width katakana';
comment on column bank_reg.banks.bank_full_kana is 'Bank name in full-width katakana';
comment on column bank_reg.banks.bank_full_hira is 'Bank name in hiragana';
comment on column bank_reg.banks.business_type_code is 'Business type code';
comment on column bank_reg.banks.business_type is 'Business type';
comment on column bank_reg.banks.dataset_id is 'Master Export dataset id that last refreshed this row';

create table bank_reg.bank_master_imports
(
    dataset_id     varchar(128)             not null,
    published_at   timestamp with time zone,
    occurred_at    timestamp with time zone not null,
    zip_sha256     varchar(64)              not null,
    bank_row_count integer                  not null,
    primary key (dataset_id)
);

create index idx_bank_master_imports_occurred_at on bank_reg.bank_master_imports (occurred_at);

comment on table bank_reg.bank_master_imports is 'bank master import event table';
comment on column bank_reg.bank_master_imports.dataset_id is 'Master Export dataset id';
comment on column bank_reg.bank_master_imports.published_at is 'Dataset published time';
comment on column bank_reg.bank_master_imports.occurred_at is 'Import completed time';
comment on column bank_reg.bank_master_imports.zip_sha256 is 'SHA-256 of the downloaded ZIP';
comment on column bank_reg.bank_master_imports.bank_row_count is 'Number of imported bank rows';
