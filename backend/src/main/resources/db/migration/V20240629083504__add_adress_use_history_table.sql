create table relationship.address_use_history
(
    address_use_id      numeric(19, 0) not null,
    party_id            numeric(19, 0) not null,
    address_Id          numeric(19, 0) not null,
    begin_date          date           not null,
    end_date            date           not null,
    type                varchar(20)    not null,
    reason_for_transfer varchar(30)    not null,
    primary key (address_use_id)
);
