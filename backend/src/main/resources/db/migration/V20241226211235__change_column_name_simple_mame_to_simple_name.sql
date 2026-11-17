alter table undecided_as.parties
    add column simple_name varchar(50);

update undecided_as.parties
set simple_name = simple_mame
where true;

ALTER TABLE undecided_as.parties
    ALTER COLUMN simple_name SET NOT NULL;

ALTER TABLE undecided_as.parties
    drop column simple_mame;
