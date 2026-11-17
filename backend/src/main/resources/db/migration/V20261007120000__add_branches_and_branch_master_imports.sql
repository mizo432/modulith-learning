create table undecided_ef.branches
(
    branch_code      varchar(3)   not null,
    bank_code        varchar(4)   not null,
    branch_name      varchar(128) not null,
    branch_half_kana varchar(256),
    branch_full_kana varchar(256),
    branch_hiragana  varchar(256),
    dataset_id       varchar(128) not null,
    primary key (bank_code, branch_code)
);

create index idx_branches_dataset_id on undecided_ef.branches (dataset_id);

comment on table undecided_ef.branches is 'bank branch table';
comment on column undecided_ef.branches.branch_code is 'Branch code (3 digits)';
comment on column undecided_ef.branches.bank_code is 'Bank code (4 digits)';
comment on column undecided_ef.branches.branch_name is 'Branch name';
comment on column undecided_ef.branches.branch_half_kana is 'Branch name in half-width katakana';
comment on column undecided_ef.branches.branch_full_kana is 'Branch name in full-width katakana';
comment on column undecided_ef.branches.branch_hiragana is 'Branch name in hiragana';
comment on column undecided_ef.branches.dataset_id is 'Master Export dataset id that last refreshed this row';

create table undecided_ef.branch_master_imports
(
    dataset_id       varchar(128)             not null,
    published_at     timestamp with time zone,
    occurred_at      timestamp with time zone not null,
    zip_sha256       varchar(64)              not null,
    branch_row_count integer                  not null,
    primary key (dataset_id)
);

create index idx_branch_master_imports_occurred_at on undecided_ef.branch_master_imports (occurred_at);

comment on table undecided_ef.branch_master_imports is 'branch master import event table';
comment on column undecided_ef.branch_master_imports.dataset_id is 'Master Export dataset id';
comment on column undecided_ef.branch_master_imports.published_at is 'Dataset published time';
comment on column undecided_ef.branch_master_imports.occurred_at is 'Import completed time';
comment on column undecided_ef.branch_master_imports.zip_sha256 is 'SHA-256 of the downloaded ZIP';
comment on column undecided_ef.branch_master_imports.branch_row_count is 'Number of imported branch rows';
