import { renderHook, act } from '@testing-library/react';
import { useNotifications } from './useNotifications';
import * as notificationService from '../services/notificationService';
import { NotificationResponse } from '../types/notification';

jest.mock('../services/notificationService');

describe('useNotifications', () => {
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

  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('未読通知を取得できる', async () => {
    (notificationService.getUnreadNotifications as jest.Mock).mockResolvedValue(mockNotifications);

    const { result } = renderHook(() => useNotifications('1'));

    await act(async () => {
      await result.current.fetchNotifications();
    });

    expect(result.current.notifications).toEqual(mockNotifications);
    expect(result.current.loading).toBe(false);
    expect(result.current.error).toBeNull();
  });

  it('通知取得に失敗したとき、エラー状態になる', async () => {
    const error = new Error('Network error');
    (notificationService.getUnreadNotifications as jest.Mock).mockRejectedValue(error);

    const { result } = renderHook(() => useNotifications('1'));

    await act(async () => {
      await result.current.fetchNotifications();
    });

    expect(result.current.error).toBe('Network error');
    expect(result.current.notifications).toEqual([]);
  });

  it('通知を既読にできる', async () => {
    (notificationService.getUnreadNotifications as jest.Mock).mockResolvedValue(mockNotifications);
    (notificationService.markAsRead as jest.Mock).mockResolvedValue(undefined);

    const { result } = renderHook(() => useNotifications('1'));

    await act(async () => {
      await result.current.fetchNotifications();
    });

    expect(result.current.notifications).toHaveLength(1);

    await act(async () => {
      await result.current.handleMarkAsRead('1');
    });

    expect(result.current.notifications).toHaveLength(0);
    expect(notificationService.markAsRead).toHaveBeenCalledWith('1');
  });

  it('employeeIdがないときは取得しない', async () => {
    const { result } = renderHook(() => useNotifications(''));

    await act(async () => {
      await result.current.fetchNotifications();
    });

    expect(notificationService.getUnreadNotifications).not.toHaveBeenCalled();
  });
});
