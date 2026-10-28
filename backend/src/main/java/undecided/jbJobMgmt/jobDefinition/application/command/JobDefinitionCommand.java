package undecided.jbJobMgmt.jobDefinition.application.command;

import undecided.jbJobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;

public interface JobDefinitionCommand {

  void register(JobDefinition jobDefinition);

  void updateParameterSpec(String jobCode, String parameterSpecJson);

  void disable(String jobCode);

  void enable(String jobCode);
}
