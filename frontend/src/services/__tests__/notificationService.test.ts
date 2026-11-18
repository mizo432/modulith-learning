import { getUnreadNotifications, markAsRead } from '../notificationService';
import { NotificationResponse } from '../../types/notification';

describe('notificationService', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    global.fetch = jest.fn();
  });

  afterEach(() => {
    delete (global as any).fetch;
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

      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
        json: () => Promise.resolve(mockNotifications),
      });

      const result = await getUnreadNotifications('1');

      expect(result).toEqual(mockNotifications);
      expect(global.fetch).toHaveBeenCalledWith('/api/notifications/unread/1');
    });

    it('APIエラーのとき、例外をスローする', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 500,
      });

      await expect(getUnreadNotifications('1')).rejects.toThrow(
        'Failed to fetch notifications: 500'
      );
    });
  });

  describe('markAsRead', () => {
    it('通知を既読にできる', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: true,
      });

      await markAsRead('1');

      expect(global.fetch).toHaveBeenCalledWith('/api/notifications/1/read', {
        method: 'PUT',
      });
    });

    it('APIエラーのとき、例外をスローする', async () => {
      (global.fetch as jest.Mock).mockResolvedValue({
        ok: false,
        status: 404,
      });

      await expect(markAsRead('1')).rejects.toThrow('Failed to mark as read: 404');
    });
  });
});
