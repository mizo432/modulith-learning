import api from './api';

// Sprint type definitions matching backend API
export interface Sprint {
  id: string;
  productId: string;
  sprintNumber: number;
  title: string;
  goal?: string;
  startDate: string; // ISO date string
  endDate: string; // ISO date string
  status: 'PLANNED' | 'ACTIVE' | 'COMPLETED' | 'CANCELLED';
  teamId: string;
  teamName?: string;
  velocity?: number;
  notes?: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateSprintRequest {
  productId: string;
  sprintNumber: number;
  title: string;
  goal?: string;
  startDate: string;
  endDate: string;
  teamId: string;
  teamName?: string;
}

export interface SprintBacklog {
  backlogId: string;
  name: string;
  description?: string;
  estimatedEffort?: number;
  remainingEffort?: number;
  sprint: {
    sprintId: string;
    name: string;
  };
  userStory?: {
    storyId: string;
    title: string;
  };
}

// Helper function to check if a sprint is Sprint 0
export function isSprint0(sprint: Sprint): boolean {
  return sprint.sprintNumber === 0;
}

// Sprint service functions
const sprintService = {
  // Get all sprints
  getAllSprints: async (): Promise<Sprint[]> => {
    const response = await api.get('/v1/sprints');
    return response.data;
  },

  // Get sprint by ID
  getSprintById: async (id: string): Promise<Sprint> => {
    const response = await api.get(`/v1/sprints/${id}`);
    return response.data;
  },

  // Create a new sprint
  createSprint: async (request: CreateSprintRequest): Promise<Sprint> => {
    const response = await api.post('/v1/sprints', request);
    return response.data;
  },

  // Update sprint status
  updateSprintStatus: async (id: string, status: Sprint['status']): Promise<Sprint> => {
    const response = await api.patch(`/v1/sprints/${id}/status`, status);
    return response.data;
  },

  // Get sprints by team ID
  getSprintsByTeamId: async (teamId: string): Promise<Sprint[]> => {
    const allSprints = await sprintService.getAllSprints();
    return allSprints.filter(sprint => sprint.teamId === teamId);
  },

  // Get sprint backlog items
  getSprintBacklogItems: async (sprintId: string): Promise<SprintBacklog[]> => {
    const response = await api.get(`/sprints/${sprintId}/backlog`);
    return response.data;
  },

  // Create a sprint backlog item
  createSprintBacklogItem: async (
      sprintId: string,
      backlogItem: Omit<SprintBacklog, 'backlogId' | 'sprint'>
  ): Promise<SprintBacklog> => {
    const response = await api.post(`/sprints/${sprintId}/backlog`, backlogItem);
    return response.data;
  },

  // Update a sprint backlog item
  updateSprintBacklogItem: async (
      backlogId: string,
      backlogItem: Partial<SprintBacklog>
  ): Promise<SprintBacklog> => {
    const response = await api.put(`/sprint-backlogs/${backlogId}`, backlogItem);
    return response.data;
  }
};

export default sprintService;
