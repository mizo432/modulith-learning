create table scrum.user_stories
(
    user_story_id            BIGINT       not null,
    feature_id               BIGINT       not null,
    actor_id                 BIGINT       not null,
    title                    varchar(256) not null,
    type                     varchar(20)  not null,
    description              varchar(1024),
    business_value           INT,
    acceptance_criteria      varchar(1024),
    priority                 INT,
    story_point              INT,
    status                   SMALLINT     not null,
    planned_business_value   INT,
    completed_business_Value INT,
    planned_story_point      INT,
    completed_story_point    INT,
    comments                 varchar(1024),
    primary key (user_story_id)
);

