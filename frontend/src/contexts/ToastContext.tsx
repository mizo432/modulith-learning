import React, {
  createContext,
  ReactNode,
  useCallback,
  useContext,
  useEffect,
  useRef,
  useState,
} from 'react';
import { Alert, AlertColor, Box, Fade } from '@mui/material';

export type ToastSeverity = AlertColor;

interface ToastContextType {
  showToast: (message: string, severity?: ToastSeverity) => void;
}

interface ToastItem {
  id: number;
  message: string;
  severity: ToastSeverity;
  open: boolean;
}

/**
 * ローカルなUIイベント用のトースト基盤。
 *
 * <p>#658トースト仕様に準拠：
 * - 表示位置: 右下（モバイルは画面下部に横幅いっぱい）
 * - 自動消去: 6秒
 * - 同時表示: 最大3件。連続する同一の警告は1件に集約する
 * - アニメーション: フェードイン/フェードアウト
 * - 閉じるボタンあり
 *
 * <p>サーバー通知用の NotificationSnackbar とは責務が異なるため分離している。
 */
const AUTO_HIDE_DURATION = 6000;
const EXIT_DURATION = 300;
const MAX_TOASTS = 3;

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const ToastProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const [toasts, setToasts] = useState<ToastItem[]>([]);
  const nextId = useRef(1);
  const timers = useRef(new Map<number, ReturnType<typeof setTimeout>[]>());

  const addTimer = useCallback((id: number, timer: ReturnType<typeof setTimeout>) => {
    const list = timers.current.get(id) ?? [];
    list.push(timer);
    timers.current.set(id, list);
  }, []);

  const removeToast = useCallback((id: number) => {
    setToasts((prev) => prev.filter((t) => t.id !== id));
    const list = timers.current.get(id);
    if (list) {
      list.forEach((timer) => clearTimeout(timer));
      timers.current.delete(id);
    }
  }, []);

  const dismissToast = useCallback(
    (id: number) => {
      setToasts((prev) => prev.map((t) => (t.id === id ? { ...t, open: false } : t)));
      addTimer(id, setTimeout(() => removeToast(id), EXIT_DURATION));
    },
    [addTimer, removeToast]
  );

  const showToast = useCallback(
    (message: string, severity: ToastSeverity = 'info') => {
      setToasts((prev) => {
        // 連続する同一の警告は1件に集約する（#658）
        if (prev.some((t) => t.open && t.message === message && t.severity === severity)) {
          return prev;
        }
        const id = nextId.current++;
        const item: ToastItem = { id, message, severity, open: true };
        const next = [...prev, item];
        // 同時表示は最大3件。最古のものから押し出す（#658）
        const trimmed =
          next.length > MAX_TOASTS ? next.slice(next.length - MAX_TOASTS) : next;
        if (trimmed.includes(item)) {
          addTimer(id, setTimeout(() => dismissToast(id), AUTO_HIDE_DURATION));
        }
        return trimmed;
      });
    },
    [addTimer, dismissToast]
  );

  useEffect(() => {
    const map = timers.current;
    return () => {
      map.forEach((list) => list.forEach((timer) => clearTimeout(timer)));
      map.clear();
    };
  }, []);

  return (
    <ToastContext.Provider value={{ showToast }}>
      {children}
      <Box
        sx={{
          position: 'fixed',
          bottom: 24,
          right: 24,
          zIndex: (theme) => theme.zIndex.snackbar,
          display: 'flex',
          flexDirection: 'column',
          gap: 1,
          '@media (max-width:600px)': {
            left: 16,
            right: 16,
            bottom: 16,
          },
        }}
      >
        {toasts.map((toast) => (
          <Fade key={toast.id} in={toast.open}>
            <Alert
              severity={toast.severity}
              variant="filled"
              onClose={() => dismissToast(toast.id)}
            >
              {toast.message}
            </Alert>
          </Fade>
        ))}
      </Box>
    </ToastContext.Provider>
  );
};

export const useToast = (): ToastContextType => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};
