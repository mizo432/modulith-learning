-- Contactテーブルを作成（TABLE_PER_CLASS継承ストラテジ）
-- 各サブクラスが独自のテーブルを持ちます

-- Contactルートテーブル（共通フィールド）
create table undecided_as.Contact
(
    contact_id bigint          not null constraint Contact_pk primary key,
    value      varchar(200),
    type       varchar(20)     not null
);

-- EmailAddressテーブル
create table undecided_as.EmailAddress
(
    contact_id bigint          not null constraint EmailAddress_pk primary key,
    constraint EmailAddress_fk
        foreign key (contact_id) references undecided_as.Contact(contact_id)
);

-- PhoneNoテーブル
create table undecided_as.PhoneNo
(
    contact_id bigint          not null constraint PhoneNo_pk primary key,
    constraint PhoneNo_fk
        foreign key (contact_id) references undecided_as.Contact(contact_id)
);

-- Addressテーブル
create table undecided_as.Address
(
    contact_id bigint          not null constraint Address_pk primary key,
    constraint Address_fk
        foreign key (contact_id) references undecided_as.Contact(contact_id)
);

-- ContactUsesテーブルにcontact_idカラムを追加
alter table undecided_as.ContactUses
    add column contact_id bigint;

alter table undecided_as.ContactUses
    add constraint ContactUses_contact_fk
        foreign key (contact_id) references undecided_as.Contact(contact_id);
