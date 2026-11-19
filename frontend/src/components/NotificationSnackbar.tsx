import React, { useState, useEffect, useCallback } from 'react';
import { Snackbar, Alert, AlertColor } from '@mui/material';
import { useNotifications } from '../hooks/useNotifications';

/**
 * 通知スナックバーコンポーネント。
 *
 * <p>#658トースト仕様に準拠：
 * - 表示位置: 右下
 * - 自動消去: 6秒
 * - 同時表示: 最大3件
 * - アニメーション: フェードイン/フェードアウト
 */

interface NotificationSnackbarProps {
  employeeId: string;
}

const MAX_VISIBLE = 3;
const AUTO_HIDE_DURATION = 6000;

export const NotificationSnackbar: React.FC<NotificationSnackbarProps> = ({ employeeId }) => {
  const { notifications, handleMarkAsRead } = useNotifications(employeeId);
  const [open, setOpen] = useState(false);
  const [currentIndex, setCurrentIndex] = useState(0);
  const [queue, setQueue] = useState(notifications.slice(0, MAX_VISIBLE));

  // 通知が更新されたらキューを更新
  useEffect(() => {
    const newQueue = notifications.slice(0, MAX_VISIBLE);
    setQueue(newQueue);
    if (newQueue.length > 0 && !open) {
      setCurrentIndex(0);
      setOpen(true);
    }
  }, [notifications]);

  const currentNotification = queue[currentIndex];

  const handleClose = useCallback(() => {
    if (currentNotification) {
      handleMarkAsRead(currentNotification.id);
    }
    if (currentIndex + 1 < queue.length) {
      setCurrentIndex(prev => prev + 1);
    } else {
      setOpen(false);
      setCurrentIndex(0);
    }
  }, [currentNotification, currentIndex, queue.length, handleMarkAsRead]);

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
      open={open}
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
