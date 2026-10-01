create schema if not exists calendar_reg;

create table calendar_reg.holiday
(
    holiday_id   BIGINT      not null,
    holiday_date date        not null,
    holiday_name varchar(50) not null,
    remarks      varchar(256),
    primary key (holiday_id)
);

create unique index holiday_date_uindex on calendar_reg.holiday (holiday_date);

comment on table calendar_reg.holiday is 'holiday table';
comment on column calendar_reg.holiday.holiday_id is 'Unique identifier for the holiday';
comment on column calendar_reg.holiday.holiday_date is 'Holiday date';
comment on column calendar_reg.holiday.holiday_name is 'Holiday name';
comment on column calendar_reg.holiday.remarks is 'Remarks';
