import api from './api';

// Sprint type definitions
export interface Sprint {
  sprintId: string;
  name: string;
  goal?: string;
  startDate: string; // ISO date string
  endDate: string; // ISO date string
  status: 'PLANNED' | 'IN_PROGRESS' | 'COMPLETED' | 'CANCELLED';
  product: {
    productId: string;
    name: string;
  };
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

// Sprint service functions
const sprintService = {
  // Get all sprints
  getAllSprints: async (): Promise<Sprint[]> => {
    const response = await api.get('/sprints');
    return response.data;
  },

  // Get sprint by ID
  getSprintById: async (id: string): Promise<Sprint> => {
    const response = await api.get(`/sprints/${id}`);
    return response.data;
  },

  // Create a new sprint
  createSprint: async (sprint: Omit<Sprint, 'sprintId'>): Promise<Sprint> => {
    const response = await api.post('/sprints', sprint);
    return response.data;
  },

  // Update an existing sprint
  updateSprint: async (id: string, sprint: Partial<Sprint>): Promise<Sprint> => {
    const response = await api.put(`/sprints/${id}`, sprint);
    return response.data;
  },

  // Start a sprint
  startSprint: async (id: string): Promise<Sprint> => {
    const response = await api.put(`/sprints/${id}/start`);
    return response.data;
  },

  // Complete a sprint
  completeSprint: async (id: string): Promise<Sprint> => {
    const response = await api.put(`/sprints/${id}/complete`);
    return response.data;
  },

  // Cancel a sprint
  cancelSprint: async (id: string): Promise<Sprint> => {
    const response = await api.put(`/sprints/${id}/cancel`);
    return response.data;
  },

  // Get sprints by product ID
  getSprintsByProductId: async (productId: string): Promise<Sprint[]> => {
    const response = await api.get(`/products/${productId}/sprints`);
    return response.data;
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
