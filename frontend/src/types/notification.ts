/**
 * 通知レスポンスの型定義。
 */
export interface NotificationResponse {
  id: string;
  backlogItemId: string;
  title: string;
  content: string;
  type: string;
  isRead: boolean;
  createdAt: string;
}
