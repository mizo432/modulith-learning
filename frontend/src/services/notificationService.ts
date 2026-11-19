import { NotificationResponse } from '../types/notification';

/**
 * 通知サービス。
 */

/**
 * 指定されたユーザーの未読通知を取得します。
 * @param employeeId 従業員ID
 * @return 未読通知のリスト
 */
export async function getUnreadNotifications(employeeId: string): Promise<NotificationResponse[]> {
  const response = await fetch(`/api/notifications/unread/${employeeId}`);
  if (!response.ok) {
    throw new Error(`Failed to fetch notifications: ${response.status}`);
  }
  return response.json();
}

/**
 * 指定された通知を既読としてマークします。
 * @param notificationId 通知ID
 */
export async function markAsRead(notificationId: string): Promise<void> {
  const response = await fetch(`/api/notifications/${notificationId}/read`, {
    method: 'PUT',
  });
  if (!response.ok) {
    throw new Error(`Failed to mark as read: ${response.status}`);
  }
}
