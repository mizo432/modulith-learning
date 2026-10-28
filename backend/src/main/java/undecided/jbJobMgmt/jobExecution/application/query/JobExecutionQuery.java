package undecided.jbJobMgmt.jobExecution.application.query;

import java.util.List;
import java.util.Optional;
import undecided.jbJobMgmt.jobExecution.domain.jobexecution.JobExecution;

public interface JobExecutionQuery {

  Optional<JobExecution> findById(String executionId);

  List<JobExecution> findAll();

  List<JobExecution> findByJobCode(String jobCode);
}
