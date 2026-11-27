import api from './api';

export type BacklogItemStatus = 'SUGGESTED' | 'ACCEPTED' | 'FROZEN' | 'READY' | 'ESTIMATED';

export type QualityCategory = 'ATTRACTIVE' | 'MUST_BE' | 'ONE_DIMENSIONAL' | 'INDIFFERENT';

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
  value?: number;
  /** StoryPoint（見積もり規模）。未設定の場合は null。 */
  storyPoint?: number | null;
  qualityCategory?: QualityCategory;
  qualityCategoryLabel?: string;
  createdAt: string;
  updatedAt?: string;
  /** 添付ファイル一覧（詳細取得時に含まれる）。 */
  attachments?: BacklogItemAttachment[];
  /** 依存先の件数（Issue #542）。 */
  dependencyCount?: number;
  /** 依存先がすべて完了しているか（Issue #542）。 */
  dependenciesSatisfied?: boolean;
}

export interface BacklogItemAttachment {
  documentId: string;
  seriesId: string;
  fileName: string;
  contentType: string;
  fileSize: number;
  version: number;
  uploaderEmployeeId?: string;
  uploaderName: string;
  createdAt: string;
}

export interface BacklogItemComment {
  id: string;
  backlogItemId: string;
  authorEmployeeId?: string;
  authorName: string;
  body: string;
  createdAt: string;
  /** 親コメントのID（返信の場合）。ルートコメントの場合はnull。 */
  parentId?: string | null;
  /** 子返信があるかどうか。 */
  hasReplies?: boolean;
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
  /** 親コメントのID（返信の場合）。 */
  parentCommentId?: string;
}

export interface UpdateBacklogItemRequest {
  title?: string;
  description?: string;
  priority?: number;
  sprintId?: string;
  value?: number;
}

export interface UpdateBacklogItemCommentRequest {
  actorEmployeeId?: string;
  body: string;
}

export interface DeleteBacklogItemCommentRequest {
  actorEmployeeId?: string;
}

/** 割り当て可能なスプリント（Issue #525） */
export interface AssignableSprint {
  sprintId: string;
  sprintNumber: number;
  title: string;
  status: string;
  startDate: string;
  endDate: string;
  /** 現在このアイテムが割り当てられているスプリントかどうか */
  assigned: boolean;
}

/** 依存関係で結ばれたバックログアイテム1件分（Issue #542） */
export interface BacklogItemDependencyEntry {
  backlogItemId: string;
  title: string;
  status: string;
  statusLabel: string;
  satisfied: boolean;
}

/** バックログアイテムの依存関係（依存先・依存元と充足状態）（Issue #542） */
export interface BacklogItemDependencies {
  backlogItemId: string;
  dependencies: BacklogItemDependencyEntry[];
  dependents: BacklogItemDependencyEntry[];
  dependenciesSatisfied: boolean;
  unmetDependencyCount: number;
}

/** StoryPoint 見積もりリクエスト（Issue #516 実装予定） */
export interface EstimateBacklogItemRequest {
  storyPoint: number;
  actorEmployeeId?: string;
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
    try {
      const response = await api.patch(`/v1/backlog-items/${backlogItemId}/status`, {
        status,
        actorEmployeeId,
      });
      return response.data;
    } catch (err: any) {
      if (err?.response?.data?.detail) {
        const normalizedError = new Error(err.response.data.detail);
        (normalizedError as any).response = err.response;
        throw normalizedError;
      }
      throw err;
    }
  },

  async updateBacklogItem(
    backlogItemId: string,
    request: UpdateBacklogItemRequest,
  ): Promise<BacklogItem> {
    try {
      const response = await api.patch(`/v1/backlog-items/${backlogItemId}`, request);
      return response.data;
    } catch (err: any) {
      if (err?.response?.data?.detail) {
        const normalizedError = new Error(err.response.data.detail);
        (normalizedError as any).response = err.response;
        throw normalizedError;
      }
      throw err;
    }
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

  async updateComment(
    backlogItemId: string,
    commentId: string,
    request: UpdateBacklogItemCommentRequest,
  ): Promise<BacklogItemComment> {
    const response = await api.patch(
      `/v1/backlog-items/${backlogItemId}/comments/${commentId}`,
      request,
    );
    return response.data;
  },

  async deleteComment(
    backlogItemId: string,
    commentId: string,
    request: DeleteBacklogItemCommentRequest,
  ): Promise<void> {
    await api.delete(`/v1/backlog-items/${backlogItemId}/comments/${commentId}`, {
      data: request,
    });
  },

  /** 割り当て可能なスプリント一覧を取得する（Issue #525） */
  async getAssignableSprints(
    backlogItemId: string,
    actorEmployeeId: string,
  ): Promise<AssignableSprint[]> {
    const response = await api.get(`/v1/backlog-items/${backlogItemId}/assignable-sprints`, {
      params: { actorEmployeeId },
    });
    return response.data;
  },

  /** スプリントへ割り当てる／割り当て先を変更する（Issue #525） */
  async assignSprint(
    backlogItemId: string,
    sprintId: string,
    actorEmployeeId: string,
  ): Promise<BacklogItem> {
    const response = await api.post(`/v1/backlog-items/${backlogItemId}/sprint-assignment`, {
      sprintId,
      actorEmployeeId,
    });
    return response.data;
  },

  /** スプリント割り当てを解除する（Issue #525） */
  async unassignSprint(
    backlogItemId: string,
    actorEmployeeId: string,
  ): Promise<BacklogItem> {
    const response = await api.delete(`/v1/backlog-items/${backlogItemId}/sprint-assignment`, {
      data: { actorEmployeeId },
    });
    return response.data;
  },

  /** 依存関係（依存先・依存元と充足状態）を取得する（Issue #542） */
  async getDependencies(backlogItemId: string): Promise<BacklogItemDependencies> {
    const response = await api.get(`/v1/backlog-items/${backlogItemId}/dependencies`);
    return response.data;
  },

  /** 依存関係を追加する（PO・SMのみ。Issue #542） */
  async addDependency(
    backlogItemId: string,
    dependencyItemId: string,
    actorEmployeeId: string,
  ): Promise<BacklogItemDependencies> {
    const response = await api.post(`/v1/backlog-items/${backlogItemId}/dependencies`, {
      dependencyItemId,
      actorEmployeeId,
    });
    return response.data;
  },

  /** 依存関係を削除する（PO・SMのみ。Issue #542） */
  async removeDependency(
    backlogItemId: string,
    dependencyItemId: string,
    actorEmployeeId: string,
  ): Promise<BacklogItemDependencies> {
    const response = await api.delete(
      `/v1/backlog-items/${backlogItemId}/dependencies/${dependencyItemId}`,
      { data: { actorEmployeeId } },
    );
    return response.data;
  },

  /** StoryPoint を見積もる（Issue #516 実装予定 — 現在はスタブ） */
  async estimateBacklogItem(
    backlogItemId: string,
    request: EstimateBacklogItemRequest,
  ): Promise<BacklogItem> {
    const response = await api.patch(`/v1/backlog-items/${backlogItemId}/estimate`, request);
    return response.data;
  },

  async getAttachments(
    backlogItemId: string,
    actorEmployeeId: string,
  ): Promise<BacklogItemAttachment[]> {
    const response = await api.get(`/v1/backlog-items/${backlogItemId}/attachments`, {
      params: { actorEmployeeId },
    });
    return response.data;
  },

  async uploadAttachment(
    backlogItemId: string,
    file: File,
    uploaderEmployeeId?: string,
    uploaderName?: string,
    seriesId?: string,
  ): Promise<BacklogItemAttachment> {
    const formData = new FormData();
    formData.append('file', file);
    if (uploaderEmployeeId) {
      formData.append('uploaderEmployeeId', uploaderEmployeeId);
    }
    if (uploaderName) {
      formData.append('uploaderName', uploaderName);
    }
    if (seriesId) {
      formData.append('seriesId', seriesId);
    }
    const response = await api.post(`/v1/backlog-items/${backlogItemId}/attachments`, formData, {
      headers: { 'Content-Type': 'multipart/form-data' },
    });
    return response.data;
  },

  async deleteAttachment(
    backlogItemId: string,
    documentId: string,
    actorEmployeeId: string,
  ): Promise<void> {
    await api.delete(`/v1/backlog-items/${backlogItemId}/attachments/${documentId}`, {
      params: { actorEmployeeId },
    });
  },

  async downloadAttachment(
    backlogItemId: string,
    documentId: string,
    actorEmployeeId: string,
  ): Promise<Blob> {
    const response = await api.get(
      `/v1/backlog-items/${backlogItemId}/attachments/${documentId}/download`,
      {
        params: { actorEmployeeId },
        responseType: 'blob',
      },
    );
    return response.data;
  },
};

export default backlogItemService;
