create table scrum.sprints
(
    sprint_id                Bigint      not null,
    product_id               Bigint      not null,
    start_date               date,
    finish_date              date,
    sprint_status            varchar(20) not null,
    planned_business_value   INT,
    completed_business_Value INT,
    planned_story_point      INT,
    completed_story_point    INT,
    comments                 varchar(1024),
    primary key (sprint_id)
);
