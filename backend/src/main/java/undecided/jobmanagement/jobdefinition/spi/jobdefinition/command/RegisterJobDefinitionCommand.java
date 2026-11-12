package undecided.jobmanagement.jobdefinition.spi.jobdefinition.command;

import undecided.jobmanagement.jobdefinition.spi.jobdefinition.dto.JobDefinitionDTO;

public interface RegisterJobDefinitionCommand {
  
  void register(JobDefinitionDTO jobDefinitionDTO);
}
