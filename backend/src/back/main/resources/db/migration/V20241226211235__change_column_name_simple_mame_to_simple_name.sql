alter table parties
    add column simple_name varchar(50);

update parties
set simple_name = simple_mame
where true;

ALTER TABLE parties
    ALTER COLUMN simple_name SET NOT NULL;

ALTER TABLE parties
    drop column simple_mame;
