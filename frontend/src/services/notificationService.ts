import { NotificationResponse } from '../types/notification';

/**
 * 通知サービス（スタブ）。
 */

/**
 * 指定されたユーザーの未読通知を取得します。
 * @param employeeId 従業員ID
 * @return 未読通知のリスト
 */
export async function getUnreadNotifications(employeeId: string): Promise<NotificationResponse[]> {
  throw new Error('Not implemented');
}

/**
 * 指定された通知を既読としてマークします。
 * @param notificationId 通知ID
 */
export async function markAsRead(notificationId: string): Promise<void> {
  throw new Error('Not implemented');
}
