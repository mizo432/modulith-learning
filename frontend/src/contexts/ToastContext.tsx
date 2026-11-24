import React, { createContext, ReactNode, useContext } from 'react';
import { AlertColor } from '@mui/material';

export type ToastSeverity = AlertColor;

interface ToastContextType {
  showToast: (message: string, severity?: ToastSeverity) => void;
}

const ToastContext = createContext<ToastContextType | undefined>(undefined);

export const ToastProvider: React.FC<{ children: ReactNode }> = ({ children }) => {
  const showToast = (_message: string, _severity?: ToastSeverity) => {
    // 段階1のRed用スタブ。本実装はGreenで入れる
  };
  return <ToastContext.Provider value={{ showToast }}>{children}</ToastContext.Provider>;
};

export const useToast = (): ToastContextType => {
  const context = useContext(ToastContext);
  if (!context) {
    throw new Error('useToast must be used within a ToastProvider');
  }
  return context;
};
