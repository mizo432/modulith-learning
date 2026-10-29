package undecided.jobmanagement.jobexecution.spi.jobexecution.command;

import undecided.jobmanagement.jobexecution.spi.jobexecution.dto.JobExecutionDTO;

public interface StartJobCommand {
  
  void startJob(JobExecutionDTO jobExecutionDTO);
}
