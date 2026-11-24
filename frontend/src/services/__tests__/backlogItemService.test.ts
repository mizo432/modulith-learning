// Mock the api module before importing backlogItemService
const mockPatch = jest.fn();
const mockGet = jest.fn();
const mockPost = jest.fn();
const mockDelete = jest.fn();
jest.mock('../api', () => ({
  __esModule: true,
  default: {
    patch: (...args: any[]) => mockPatch(...args),
    get: (...args: any[]) => mockGet(...args),
    post: (...args: any[]) => mockPost(...args),
    delete: (...args: any[]) => mockDelete(...args),
  },
}));

import backlogItemService, { BacklogItem, BacklogItemStatus } from '../backlogItemService';

describe('backlogItemService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('updateBacklogItem', () => {
    const mockResponse: BacklogItem = {
      id: '100',
      scrumTeamId: 'team-1',
      title: 'テストアイテム',
      description: '説明',
      status: 'SUGGESTED',
      statusLabel: '提案',
      priority: 1,
      creatorName: 'テストユーザー',
      createdAt: '2026-01-01T00:00:00Z',
      updatedAt: '2026-01-01T01:00:00Z',
    };

    it('should call PATCH /v1/backlog-items/{id} with correct request body', async () => {
      mockPatch.mockResolvedValue({ data: mockResponse });

      const result = await backlogItemService.updateBacklogItem('100', {
        value: 50,
      });

      expect(mockPatch).toHaveBeenCalledWith('/v1/backlog-items/100', { value: 50 });
      expect(result).toEqual(mockResponse);
    });

    it('should call PATCH with multiple fields', async () => {
      mockPatch.mockResolvedValue({ data: mockResponse });

      const result = await backlogItemService.updateBacklogItem('100', {
        title: '更新タイトル',
        value: 75,
        priority: 2,
      });

      expect(mockPatch).toHaveBeenCalledWith('/v1/backlog-items/100', {
        title: '更新タイトル',
        value: 75,
        priority: 2,
      });
      expect(result).toEqual(mockResponse);
    });
  });

  describe('changeStatus', () => {
    it('should propagate HTTP 400 error', async () => {
      const error: any = new Error('Bad Request');
      error.response = {
        status: 400,
        data: { message: 'BacklogItem value must be set before accepting' },
      };
      mockPatch.mockRejectedValue(error);

      await expect(
        backlogItemService.changeStatus('100', 'ACCEPTED' as BacklogItemStatus, 'emp-1')
      ).rejects.toThrow('BacklogItem value must be set before accepting');
    });

    it('should propagate HTTP 400 error with value range message', async () => {
      const error: any = new Error('Bad Request');
      error.response = {
        status: 400,
        data: { message: 'BacklogItem value must be between 0 and 100' },
      };
      mockPatch.mockRejectedValue(error);

      await expect(
        backlogItemService.changeStatus('100', 'ACCEPTED' as BacklogItemStatus, 'emp-1')
      ).rejects.toThrow('BacklogItem value must be between 0 and 100');
    });
  });

  describe('estimateBacklogItem', () => {
    const mockResponse: BacklogItem = {
      id: '100',
      scrumTeamId: 'team-1',
      title: 'テストアイテム',
      description: '説明',
      status: 'ESTIMATED',
      statusLabel: '見積もり済み',
      priority: 1,
      creatorName: 'テストユーザー',
      createdAt: '2026-01-01T00:00:00Z',
      updatedAt: '2026-01-01T01:00:00Z',
      storyPoint: 5,
    };

    it('should call PATCH /v1/backlog-items/{id}/estimate with correct request body', async () => {
      mockPatch.mockResolvedValue({ data: mockResponse });

      const result = await backlogItemService.estimateBacklogItem('100', {
        storyPoint: 5,
        actorEmployeeId: 'emp-1',
      });

      expect(mockPatch).toHaveBeenCalledWith('/v1/backlog-items/100/estimate', {
        storyPoint: 5,
        actorEmployeeId: 'emp-1',
      });
      expect(result).toEqual(mockResponse);
    });

    it('should return BacklogItem type with storyPoint field', async () => {
      const mockResponseWithSp13: BacklogItem = {
        ...mockResponse,
        storyPoint: 13,
      };
      mockPatch.mockResolvedValue({ data: mockResponseWithSp13 });

      const result = await backlogItemService.estimateBacklogItem('100', {
        storyPoint: 13,
      });

      expect(result.storyPoint).toBe(13);
      expect(result.status).toBe('ESTIMATED');
    });
  });

  describe('sprint assignment (Issue #525)', () => {
    it('getAssignableSprints should call GET /v1/backlog-items/{id}/assignable-sprints with actorEmployeeId', async () => {
      const sprints = [
        {
          sprintId: 'sprint-1',
          sprintNumber: 1,
          title: 'Sprint 1',
          status: 'ACTIVE',
          startDate: '2026-11-16',
          endDate: '2026-11-27',
          assigned: false,
        },
      ];
      mockGet.mockResolvedValue({ data: sprints });

      const result = await backlogItemService.getAssignableSprints('100', 'emp-1');

      expect(mockGet).toHaveBeenCalledWith('/v1/backlog-items/100/assignable-sprints', {
        params: { actorEmployeeId: 'emp-1' },
      });
      expect(result).toEqual(sprints);
    });

    it('assignSprint should call POST /v1/backlog-items/{id}/sprint-assignment with sprintId and actorEmployeeId', async () => {
      mockPost.mockResolvedValue({ data: { id: '100', sprintId: 'sprint-1' } });

      const result = await backlogItemService.assignSprint('100', 'sprint-1', 'emp-1');

      expect(mockPost).toHaveBeenCalledWith('/v1/backlog-items/100/sprint-assignment', {
        sprintId: 'sprint-1',
        actorEmployeeId: 'emp-1',
      });
      expect(result).toEqual({ id: '100', sprintId: 'sprint-1' });
    });

    it('unassignSprint should call DELETE /v1/backlog-items/{id}/sprint-assignment with actorEmployeeId in body', async () => {
      mockDelete.mockResolvedValue({ data: { id: '100' } });

      const result = await backlogItemService.unassignSprint('100', 'emp-1');

      expect(mockDelete).toHaveBeenCalledWith('/v1/backlog-items/100/sprint-assignment', {
        data: { actorEmployeeId: 'emp-1' },
      });
      expect(result).toEqual({ id: '100' });
    });
  });
});
