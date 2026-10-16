create schema if not exists undecided_as;

create table if not exists undecided_as.companies
(
    party_id     BIGINT       not null,
    company_code varchar(3)   not null,
    company_name varchar(128) not null,
    primary key (party_id)
);
