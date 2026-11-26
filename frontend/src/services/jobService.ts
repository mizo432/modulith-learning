import api from './api';

export interface JobDefinition {
  id: string;
  jobCode: string;
  version?: string;
  parameterSpec?: string;
  maxConcurrentExecutions?: number;
  timeoutSeconds?: number;
  isActive: boolean;
}

export interface JobExecution {
  id: string;
  executionId: string;
  jobCode: string;
  state: string;
  startedAt?: string;
  completedAt?: string;
  errorMessage?: string;
  createdBy?: string;
}

export interface StartJobRequest {
  jobCode: string;
  actorEmployeeId: string;
  parameters?: string;
  executionId?: string;
}

export const getJobDefinitions = async (): Promise<JobDefinition[]> => {
  const response = await api.get('/job-definitions');
  return response.data;
};

export const getJobExecutions = async (): Promise<JobExecution[]> => {
  const response = await api.get('/job-executions');
  return response.data;
};

export const startJob = async (request: StartJobRequest): Promise<void> => {
  await api.post('/job-executions', request);
};
