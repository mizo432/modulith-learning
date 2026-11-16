package undecided.agiloom.requirementBacklog.internal;

import org.springframework.data.repository.CrudRepository;
import undecided.supporting.snowflake.SnowflakeId;

interface ScrumTeamSubsetRepository extends CrudRepository<ScrumTeamSubset, SnowflakeId> {}
