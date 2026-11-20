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

  // === Issue #706 対応テスト ===

  it('既読化に成功したとき、進行中の取得結果が適用されない', async () => {
    // テストリスト項目14
    // 2回目の取得が既読化前の古い一覧を返すシナリオ：
    // 初期取得で通知1件 → 2回目の取得を開始（Promise未解決） → その間に既読化成功（通知0件）
    // → 保持していた取得Promiseが古い一覧で解決 → 最終的に通知が0件のまま
    const initialNotifications: NotificationResponse[] = [
      {
        id: '1',
        backlogItemId: '100',
        title: '既読にする通知',
        content: '内容',
        type: 'COMMENT_ADDED',
        isRead: false,
        createdAt: '2025-01-01T00:00:00',
      },
    ];

    // 2回目のリクエストは遅延させる（Promiseを未解決のまま保持）
    let resolveSecond: () => void;
    const secondPromise = new Promise<NotificationResponse[]>((resolve) => {
      resolveSecond = () => resolve(initialNotifications);
    });

    // useEffectによる初期取得(1回目)は即座に完了、手動fetch(2回目)は遅延、既読化は即座に完了
    (notificationService.getUnreadNotifications as jest.Mock)
      .mockReturnValueOnce(Promise.resolve(initialNotifications))
      .mockReturnValueOnce(secondPromise)
      .mockResolvedValue([]);
    (notificationService.markAsRead as jest.Mock).mockResolvedValue(undefined);

    const { result } = renderHook(() => useNotifications('1'));

    // useEffectによる初期取得で通知1件が表示される
    await act(async () => {
      await Promise.resolve();
      await Promise.resolve();
    });

    expect(result.current.notifications).toHaveLength(1);

    // 2回目の取得を開始してPromiseを未解決のまま保持する
    const fetch2Promise = result.current.fetchNotifications();

    // その間に handleMarkAsRead が成功して通知が0件になる
    await act(async () => {
      await result.current.handleMarkAsRead('1');
    });

    expect(result.current.notifications).toHaveLength(0);

    // 保持していた取得Promiseを既読化前の古い一覧で解決する
    await act(async () => {
      resolveSecond!();
      await fetch2Promise;
    });

    // 最終的に通知が0件のままであることを確認する（世代管理により古い結果が上書きされない）
    expect(result.current.notifications).toHaveLength(0);
  });

  it('古い取得結果が返ってきたとき、新しい状態が上書きされない', async () => {
    // テストリスト項目13
    const firstNotifications: NotificationResponse[] = [
      {
        id: '1',
        backlogItemId: '100',
        title: '古い通知',
        content: '古い内容',
        type: 'COMMENT_ADDED',
        isRead: false,
        createdAt: '2025-01-01T00:00:00',
      },
    ];
    const secondNotifications: NotificationResponse[] = [
      {
        id: '2',
        backlogItemId: '200',
        title: '新しい通知',
        content: '新しい内容',
        type: 'COMMENT_ADDED',
        isRead: false,
        createdAt: '2025-01-02T00:00:00',
      },
    ];

    // 初回取得（マウント時の useEffect による1回目のリクエスト）は遅延させる
    let resolveFirst: () => void;
    const firstPromise = new Promise<NotificationResponse[]>((resolve) => {
      resolveFirst = () => resolve(firstNotifications);
    });

    // 2回目以降のリクエストは即座に完了する
    (notificationService.getUnreadNotifications as jest.Mock)
      .mockReturnValueOnce(firstPromise)
      .mockResolvedValue(secondNotifications);

    const { result } = renderHook(() => useNotifications('1'));

    // 2回目のfetchを即座に実行し、新しい状態になる
    await act(async () => {
      await result.current.fetchNotifications();
    });
    expect(result.current.notifications).toEqual(secondNotifications);

    // 遅延していた初回取得が古い結果で完了しても、新しい状態が上書きされない
    await act(async () => {
      resolveFirst!();
    });

    expect(result.current.notifications).toEqual(secondNotifications);
  });
});
