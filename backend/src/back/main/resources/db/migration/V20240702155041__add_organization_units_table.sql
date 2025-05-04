create table if not exists organization_units
(
    party_id        BIGINT       not null,
    organization_id BIGINT       not null,
    name            varchar(128) not null,
    begin_date      date         not null,
    end_date        date         not null,
    primary key (party_id)
);
