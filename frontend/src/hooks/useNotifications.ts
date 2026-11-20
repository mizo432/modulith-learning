import { useState, useEffect, useCallback, useRef } from 'react';
import { NotificationResponse } from '../types/notification';
import { getUnreadNotifications, markAsRead } from '../services/notificationService';

/**
 * 通知のポーリングと状態管理を行うhook。
 * 30秒間隔で未読通知をポーリングし、スナックバー表示用データを管理します。
 */
export function useNotifications(employeeId: string) {
  const [notifications, setNotifications] = useState<NotificationResponse[]>([]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  // リクエスト世代管理（古い取得結果の上書きを防止）
  const requestGeneration = useRef(0);

  const fetchNotifications = useCallback(async () => {
    if (!employeeId) return;

    // 取得開始時に世代を進める
    requestGeneration.current += 1;
    const myGeneration = requestGeneration.current;

    setLoading(true);
    setError(null);
    try {
      const data = await getUnreadNotifications(employeeId);
      // 結果を反映する前に、自分の世代が最新か確認する
      if (myGeneration === requestGeneration.current) {
        setNotifications(data);
      }
    } catch (err) {
      if (myGeneration === requestGeneration.current) {
        setError(err instanceof Error ? err.message : 'Failed to fetch notifications');
        setNotifications([]);
      }
    } finally {
      if (myGeneration === requestGeneration.current) {
        setLoading(false);
      }
    }
  }, [employeeId]);

  // 初期取得と30秒ポーリング
  useEffect(() => {
    fetchNotifications();
    const interval = setInterval(fetchNotifications, 30000);
    return () => clearInterval(interval);
  }, [fetchNotifications]);

  const handleMarkAsRead = useCallback(
    async (notificationId: string) => {
      try {
        await markAsRead(notificationId);
        setNotifications(prev => prev.filter(n => n.id !== notificationId));
        // 既読化に成功したら、進行中の取得を無効化（世代を進める）
        requestGeneration.current += 1;
      } catch (err) {
        setError(err instanceof Error ? err.message : 'Failed to mark as read');
      }
    },
    []
  );

  return {
    notifications,
    loading,
    error,
    fetchNotifications,
    handleMarkAsRead,
  };
}
