create schema if not exists undecided_ef;

create table undecided_ef.holiday
(
    holiday_id   BIGINT      not null,
    holiday_date date        not null,
    holiday_name varchar(50) not null,
    remarks      varchar(256),
    primary key (holiday_id)
);

create unique index holiday_date_uindex on undecided_ef.holiday (holiday_date);

comment on table undecided_ef.holiday is 'holiday table';
comment on column undecided_ef.holiday.holiday_id is 'Unique identifier for the holiday';
comment on column undecided_ef.holiday.holiday_date is 'Holiday date';
comment on column undecided_ef.holiday.holiday_name is 'Holiday name';
comment on column undecided_ef.holiday.remarks is 'Remarks';
