import api from './api';

export type BacklogItemStatus = 'SUGGESTED' | 'ACCEPTED' | 'REJECTED';

export interface BacklogItem {
  id: string;
  scrumTeamId: string;
  title: string;
  description?: string;
  status: BacklogItemStatus;
  statusLabel: string;
  priority?: number;
  creatorEmployeeId?: string;
  creatorName: string;
  sprintId?: string;
  createdAt: string;
  updatedAt?: string;
}

export interface BacklogItemComment {
  id: string;
  backlogItemId: string;
  authorEmployeeId?: string;
  authorName: string;
  body: string;
  createdAt: string;
}

export interface CreateBacklogItemRequest {
  title: string;
  description?: string;
  priority?: number;
  creatorEmployeeId?: string;
  creatorName: string;
}

export interface AddBacklogItemCommentRequest {
  authorEmployeeId?: string;
  authorName: string;
  body: string;
}

const backlogItemService = {
  async getBacklogItemsByTeam(
    scrumTeamId: string,
    status?: BacklogItemStatus,
  ): Promise<BacklogItem[]> {
    const response = await api.get(`/v1/scrum-teams/${scrumTeamId}/backlog-items`, {
      params: status ? { status } : undefined,
    });
    return response.data;
  },

  async getBacklogItem(backlogItemId: string): Promise<BacklogItem> {
    const response = await api.get(`/v1/backlog-items/${backlogItemId}`);
    return response.data;
  },

  async createBacklogItem(
    scrumTeamId: string,
    request: CreateBacklogItemRequest,
  ): Promise<BacklogItem> {
    const response = await api.post(
      `/v1/scrum-teams/${scrumTeamId}/backlog-items`,
      request,
    );
    return response.data;
  },

  async changeStatus(
    backlogItemId: string,
    status: BacklogItemStatus,
    actorEmployeeId: string,
  ): Promise<BacklogItem> {
    const response = await api.patch(`/v1/backlog-items/${backlogItemId}/status`, {
      status,
      actorEmployeeId,
    });
    return response.data;
  },

  async getComments(backlogItemId: string): Promise<BacklogItemComment[]> {
    const response = await api.get(`/v1/backlog-items/${backlogItemId}/comments`);
    return response.data;
  },

  async addComment(
    backlogItemId: string,
    request: AddBacklogItemCommentRequest,
  ): Promise<BacklogItemComment> {
    const response = await api.post(
      `/v1/backlog-items/${backlogItemId}/comments`,
      request,
    );
    return response.data;
  },
};

export default backlogItemService;
