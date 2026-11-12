package undecided.jobmanagement.jobexecution.spi.jobexecution.command;

import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;
import undecided.supporting.snowflake.SnowflakeId;

public interface StartJobCommand {
  
  SnowflakeId startJob(JobExecutionDTO jobExecutionDTO);
}
