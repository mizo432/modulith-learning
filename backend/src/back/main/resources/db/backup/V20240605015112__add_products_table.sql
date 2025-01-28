create table scrum.products
(
    product_id       BIGINT       not null,
    product_code     varchar(3)   not null,
    product_name     varchar(256) not null,
    description      varchar(1024),
    product_owner_id BIGINT,
    is_archived      bit,
    primary key (product_id)
);

create unique index uk1_products
    on scrum.products
        (
         product_code
            );

comment on table scrum.products is 'Product table';
comment on column scrum.products.product_id is 'Unique identifier for the product';
comment on column scrum.products.product_code is 'Code of the product';
comment on column scrum.products.product_name is 'Name of the product';
comment on column scrum.products.description is 'Description of the product';
comment on column scrum.products.product_owner_id is 'ID of the product owner';
comment on column scrum.products.is_archived is 'Flag indicating if the product is archived';
