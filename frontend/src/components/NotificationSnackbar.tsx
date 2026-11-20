import React, { useState, useEffect, useCallback } from 'react';
import { Snackbar, Alert, AlertColor } from '@mui/material';
import { useNotifications } from '../hooks/useNotifications';
import { NotificationResponse } from '../types/notification';

/**
 * 通知スナックバーコンポーネント。
 *
 * <p>#658トースト仕様に準拠：
 * - 表示位置: 右下
 * - 自動消去: 6秒
 * - 一度に1件表示
 * - アニメーション: フェードイン/フェードアウト
 */

interface NotificationSnackbarProps {
  employeeId: string;
}

const AUTO_HIDE_DURATION = 6000;

export const NotificationSnackbar: React.FC<NotificationSnackbarProps> = ({ employeeId }) => {
  const { notifications, handleMarkAsRead } = useNotifications(employeeId);
  // ローカルキューで通知を管理（propsのnotificationsがまだ変わっていない状態でも、
  // 閉じたら次の通知が表示されるようにする）
  const [queue, setQueue] = useState<NotificationResponse[]>([]);

  // 通知が更新されたらローカルキューを更新
  useEffect(() => {
    setQueue(notifications);
  }, [notifications]);

  const currentNotification = queue[0];

  const handleClose = useCallback(
    (event?: any) => {
      // 閉じる操作が詳細遷移へ伝播しないようにする
      if (event) {
        event.stopPropagation();
      }
      if (currentNotification) {
        handleMarkAsRead(currentNotification.id);
      }
      // 先頭を取り除いて次の通知を表示
      setQueue(prev => prev.slice(1));
    },
    [currentNotification, handleMarkAsRead]
  );

  const handleClick = useCallback(() => {
    if (currentNotification) {
      window.location.href = `/products/backlog/${currentNotification.backlogItemId}`;
    }
  }, [currentNotification]);

  const getAlertColor = (type: string): AlertColor => {
    switch (type) {
      case 'COMMENT_ADDED':
        return 'info';
      case 'COMMENT_DELETED':
        return 'warning';
      default:
        return 'info';
    }
  };

  if (!currentNotification) {
    return null;
  }

  return (
    <Snackbar
      key={currentNotification.id}
      open={queue.length > 0}
      autoHideDuration={AUTO_HIDE_DURATION}
      onClose={handleClose}
      anchorOrigin={{ vertical: 'bottom', horizontal: 'right' }}
    >
      <div
        role="alert"
        onClick={handleClick}
        style={{ cursor: 'pointer' }}
      >
        <Alert
          severity={getAlertColor(currentNotification.type)}
          variant="filled"
          onClose={handleClose}
        >
          <strong>{currentNotification.title}</strong>
          <br />
          {currentNotification.content}
        </Alert>
      </div>
    </Snackbar>
  );
};

export default NotificationSnackbar;
