// Mock the api module before importing backlogItemService
const mockPatch = jest.fn();
const mockGet = jest.fn();
jest.mock('../api', () => ({
  __esModule: true,
  default: {
    patch: (...args: any[]) => mockPatch(...args),
    get: (...args: any[]) => mockGet(...args),
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
});
