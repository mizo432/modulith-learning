import { NotificationResponse } from '../types/notification';
import api from './api';

/**
 * 通知サービス。
 * 共有 API クライアント（axios）を利用し、認証ヘッダー付与と401処理を一元化する。
 */

/**
 * 指定されたユーザーの未読通知を取得します。
 * @param employeeId 従業員ID
 * @return 未読通知のリスト
 */
export async function getUnreadNotifications(employeeId: string): Promise<NotificationResponse[]> {
  const response = await api.get(`/notifications/unread/${employeeId}`);
  return response.data;
}

/**
 * 指定された通知を既読としてマークします。
 * @param notificationId 通知ID
 */
export async function markAsRead(notificationId: string): Promise<void> {
  await api.put(`/notifications/${notificationId}/read`);
}
