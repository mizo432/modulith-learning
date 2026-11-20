import { getUnreadNotifications, markAsRead } from '../notificationService';
import { NotificationResponse } from '../../types/notification';

// api.ts のモック
jest.mock('../api', () => ({
  __esModule: true,
  default: {
    get: jest.fn(),
    put: jest.fn(),
  },
}));

import api from '../api';

describe('notificationService', () => {
  const mockApi = api as jest.Mocked<typeof api>;

  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe('getUnreadNotifications', () => {
    it('未読通知を取得できる', async () => {
      const mockNotifications: NotificationResponse[] = [
        {
          id: '1',
          backlogItemId: '100',
          title: 'コメントが追加されました',
          content: '新しいコメントがあります',
          type: 'COMMENT_ADDED',
          isRead: false,
          createdAt: '2025-01-01T00:00:00',
        },
      ];

      mockApi.get.mockResolvedValue({ data: mockNotifications });

      const result = await getUnreadNotifications('1');

      expect(result).toEqual(mockNotifications);
      expect(mockApi.get).toHaveBeenCalledWith('/notifications/unread/1');
    });

    it('APIエラーのとき、例外をスローする', async () => {
      mockApi.get.mockRejectedValue(new Error('API error'));

      await expect(getUnreadNotifications('1')).rejects.toThrow();
    });
  });

  describe('markAsRead', () => {
    it('通知を既読にできる', async () => {
      mockApi.put.mockResolvedValue({ data: {} });

      await markAsRead('1');

      expect(mockApi.put).toHaveBeenCalledWith('/notifications/1/read');
    });

    it('APIエラーのとき、例外をスローする', async () => {
      mockApi.put.mockRejectedValue(new Error('API error'));

      await expect(markAsRead('1')).rejects.toThrow();
    });
  });

  // === Issue #706 対応テスト ===

  it('未読通知を取得したとき、共有 API クライアントが呼ばれる', async () => {
    // テストリスト項目15
    mockApi.get.mockResolvedValue({ data: [] });

    await getUnreadNotifications('1');

    expect(mockApi.get).toHaveBeenCalledWith('/notifications/unread/1');
  });

  it('通知を既読にしたとき、共有 API クライアントが呼ばれる', async () => {
    // テストリスト項目16
    mockApi.put.mockResolvedValue({ data: {} });

    await markAsRead('1');

    expect(mockApi.put).toHaveBeenCalledWith('/notifications/1/read');
  });

  it.skip('API が401を返したとき、ログイン画面へ遷移する', async () => {
    // テストリスト項目17
    // 共有 api クライアントの response interceptor が 401 を処理するため、
    // notificationService のテストではモックに置き換わってしまう。
    // このテストはスキップし、理由を報告する。
    // （共有 api クライアントの振る舞いは api.ts のテストで検証する）
  });
});
