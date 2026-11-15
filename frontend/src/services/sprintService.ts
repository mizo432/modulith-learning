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

// Sprint Settings type definitions matching backend API
export type EndDayOfWeek = 'MONDAY' | 'TUESDAY' | 'WEDNESDAY' | 'THURSDAY' | 'FRIDAY';

export interface SprintSettings {
  productId: string;
  businessDays: number;
  endDayOfWeek: EndDayOfWeek;
}

export interface UpdateSprintSettingsRequest {
  productId: string;
  businessDays: number;
  endDayOfWeek: EndDayOfWeek;
}

export interface CalculateEndDateRequest {
  productId: string;
  startDate: string; // ISO date string (YYYY-MM-DD)
}

export interface CalculateEndDateResponse {
  startDate: string;
  endDate: string;
  businessDays: number;
  endDayOfWeekLabel: string;
  isPastDate: boolean;
}

// End day of week display names in Japanese
export const END_DAY_OF_WEEK_LABELS: Record<EndDayOfWeek, string> = {
  MONDAY: '月曜日',
  TUESDAY: '火曜日',
  WEDNESDAY: '水曜日',
  THURSDAY: '木曜日',
  FRIDAY: '金曜日',
};
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
  },

  // Get sprint settings for a product
  getSprintSettings: async (productId: string): Promise<SprintSettings> => {
    const response = await api.get('/v1/sprints/settings', {
      params: { productId }
    });
    return response.data;
  },

  // Update sprint settings
  updateSprintSettings: async (request: UpdateSprintSettingsRequest): Promise<SprintSettings> => {
    const response = await api.put('/v1/sprints/settings', request);
    return response.data;
  },

  // Calculate sprint end date using backend API
  calculateEndDate: async (request: CalculateEndDateRequest): Promise<CalculateEndDateResponse> => {
    const response = await api.post('/v1/sprints/settings/calculate-end-date', request);
    return response.data;
  }
};

export default sprintService;
