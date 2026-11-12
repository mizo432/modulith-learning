package undecided.jobmanagement.jobdefinition.spi.jobdefinition.command;

import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;
import undecided.supporting.snowflake.SnowflakeId;

public interface RegisterJobDefinitionCommandService {
  
  SnowflakeId register(JobDefinitionDTO jobDefinitionDTO);
}
