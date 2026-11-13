-- Partyのリファクタリング: bank_accounts, bank_account_use, phone_numbers テーブルを追加

-- bank_accountsテーブルを作成
create table undecided_as.bank_accounts
(
    bank_account_id bigint          not null constraint bank_accounts_pk primary key,
    bank_id         bigint          not null,
    bank_branch_id  bigint          not null,
    type            varchar(20)     not null,
    account_number  varchar(7)      not null,
    holders_name    varchar(100)    not null
);

-- bank_account_useテーブルを作成
create table undecided_as.bank_account_use
(
    bank_account_use_id bigint          not null constraint bank_account_use_pk primary key,
    bank_account_id     bigint          not null,
    party_id            bigint          not null,
    type                varchar(20)     not null,
    when_occured        timestamp
);

-- phone_numbersテーブルを作成
create table undecided_as.phone_numbers
(
    phone_number_id bigint          not null constraint phone_numbers_pk primary key,
    number          varchar(20)     not null,
    type            varchar(20)     not null
);
