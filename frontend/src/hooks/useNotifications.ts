import { useState, useEffect, useCallback } from 'react';
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

  const fetchNotifications = useCallback(async () => {
    if (!employeeId) return;

    setLoading(true);
    setError(null);
    try {
      const data = await getUnreadNotifications(employeeId);
      setNotifications(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : 'Failed to fetch notifications');
      setNotifications([]);
    } finally {
      setLoading(false);
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
