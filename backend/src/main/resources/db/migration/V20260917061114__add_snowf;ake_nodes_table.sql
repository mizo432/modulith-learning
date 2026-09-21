create table id_mgmt.snowflake_nodes
(
    node_id       integer primary key,
    instance_name varchar(100) not null,
    lease_until   timestamptz  not null
);

create index ix01_snowflake_node_lease_until on id_mgmt.snowflake_nodes (lease_until);
