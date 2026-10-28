package undecided.jbJobMgmt.jobExecution.application.command;

import undecided.jbJobMgmt.jobExecution.spi.jobexecution.JobExecution;

public interface JobExecutionCommand {

  void startJob(JobExecution jobExecution);

  void cancel(String executionId);

  void reExecute(String executionId);

  void resume(String executionId);
}
