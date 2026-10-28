package undecided.jobMgmt.jobExecution.application.command;

import undecided.jobMgmt.jobExecution.spi.jobexecution.JobExecution;

public interface JobExecutionCommand {

  void startJob(JobExecution jobExecution);

  void cancel(String executionId);

  void reExecute(String executionId);

  void resume(String executionId);
}
