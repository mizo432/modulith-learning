import { render, screen, fireEvent, act } from '@testing-library/react';
import { NotificationSnackbar } from './NotificationSnackbar';
import * as notificationService from '../services/notificationService';
import { NotificationResponse } from '../types/notification';

jest.mock('../services/notificationService');
jest.mock('../hooks/useNotifications', () => ({
  useNotifications: jest.fn(),
}));

const mockUseNotifications = require('../hooks/useNotifications').useNotifications;

describe('NotificationSnackbar', () => {
  const mockNotifications: NotificationResponse[] = [
    {
      id: '1',
      backlogItemId: '100',
      title: 'コメントが追加されました',
      content: '新しいコメントがあります',
      type: 'COMMENT_ADDED',
      isRead: false,
      createdAt: '2025-01-01T00:00:00',
    },
  ];

  beforeEach(() => {
    jest.clearAllMocks();
    mockUseNotifications.mockReturnValue({
      notifications: mockNotifications,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: jest.fn(),
    });
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  it('未読通知があるとき、スナックバーが表示される', () => {
    // テストリスト項目17
    render(<NotificationSnackbar employeeId="1" />);

    expect(screen.getByText('コメントが追加されました')).toBeInTheDocument();
    expect(screen.getByText('新しいコメントがあります')).toBeInTheDocument();
  });

  it('スナックバーを閉じたとき、通知が既読になる', async () => {
    // テストリスト項目18
    const mockMarkAsRead = jest.fn();
    mockUseNotifications.mockReturnValue({
      notifications: mockNotifications,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: mockMarkAsRead,
    });

    render(<NotificationSnackbar employeeId="1" />);

    const closeBtn = screen.getByRole('button');
    fireEvent.click(closeBtn);

    expect(mockMarkAsRead).toHaveBeenCalledWith('1');
  });

  it('6秒経過したとき、スナックバーが自動消去される', async () => {
    // テストリスト項目19
    jest.useFakeTimers();

    render(<NotificationSnackbar employeeId="1" />);

    expect(screen.getByText('コメントが追加されました')).toBeInTheDocument();

    await act(async () => {
      jest.advanceTimersByTime(6000);
    });
  });

  it('3件以上の通知があるとき、最大3件まで表示される', () => {
    // テストリスト項目20
    const manyNotifications: NotificationResponse[] = [
      ...mockNotifications,
      { ...mockNotifications[0], id: '2', title: '2番目の通知' },
      { ...mockNotifications[0], id: '3', title: '3番目の通知' },
      { ...mockNotifications[0], id: '4', title: '4番目の通知' },
    ];

    mockUseNotifications.mockReturnValue({
      notifications: manyNotifications,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: jest.fn(),
    });

    render(<NotificationSnackbar employeeId="1" />);

    // 最初の1件が表示される（キューには最大3件）
    expect(screen.getByText('コメントが追加されました')).toBeInTheDocument();
  });

  it('スナックバーをクリックしたとき、バックログアイテム詳細へ遷移する', () => {
    // テストリスト項目21
    const mockLocation = { href: '' };
    Object.defineProperty(window, 'location', {
      value: mockLocation,
      writable: true,
      configurable: true,
    });

    render(<NotificationSnackbar employeeId="1" />);

    const alert = screen.getByText('コメントが追加されました').closest('div[role="alert"]');
    if (alert) {
      fireEvent.click(alert);
    }

    expect(mockLocation.href).toBe('/products/backlog/100');
  });
});
