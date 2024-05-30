create table "relationship"."parties"
(
    party_id         numeric(19, 0) not null,
    party_type       varchar(10)    not null,
    govt_assigned_id varchar(13)    null,
    primary key (party_id)

);