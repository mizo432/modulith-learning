package undecided.jobMgmt.jobDefinition.application.command;

import undecided.jobMgmt.jobDefinition.spi.jobdefinition.JobDefinition;

public interface JobDefinitionCommand {

  void register(JobDefinition jobDefinition);

  void updateParameterSpec(String jobCode, String parameterSpecJson);

  void disable(String jobCode);

  void enable(String jobCode);
}
