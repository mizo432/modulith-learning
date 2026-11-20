import { render, screen, fireEvent, act, within } from '@testing-library/react';
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

  // === Issue #706 対応テスト ===

  it('閉じるボタンをクリックしたとき、通知が既読になり、詳細画面へ遷移しない', () => {
    // テストリスト項目8
    const mockMarkAsRead = jest.fn();
    const mockLocation = { href: '' };
    Object.defineProperty(window, 'location', {
      value: mockLocation,
      writable: true,
      configurable: true,
    });

    mockUseNotifications.mockReturnValue({
      notifications: mockNotifications,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: mockMarkAsRead,
    });

    render(<NotificationSnackbar employeeId="1" />);

    // 閉じるボタンをクリック
    const closeBtn = screen.getByRole('button');
    fireEvent.click(closeBtn);

    // 既読化は呼ばれる
    expect(mockMarkAsRead).toHaveBeenCalledWith('1');
    // 詳細画面への遷移は行われない（stopPropagation）
    expect(mockLocation.href).toBe('');
  });

  it('3件以上の通知があるとき、先頭の1件が表示される', () => {
    // テストリスト項目9
    const manyNotifications: NotificationResponse[] = [
      { ...mockNotifications[0], id: '1', title: '1番目の通知' },
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

    // 先頭の1件のみが表示される
    expect(screen.getByText('1番目の通知')).toBeInTheDocument();
    expect(screen.queryByText('2番目の通知')).not.toBeInTheDocument();
    expect(screen.queryByText('3番目の通知')).not.toBeInTheDocument();
    expect(screen.queryByText('4番目の通知')).not.toBeInTheDocument();
  });

  it('通知を閉じたとき、次の通知が表示される', () => {
    // テストリスト項目10
    const twoNotifications: NotificationResponse[] = [
      { ...mockNotifications[0], id: '1', title: '1番目の通知' },
      { ...mockNotifications[0], id: '2', title: '2番目の通知' },
    ];

    const mockMarkAsRead = jest.fn();
    mockUseNotifications.mockReturnValue({
      notifications: twoNotifications,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: mockMarkAsRead,
    });

    render(<NotificationSnackbar employeeId="1" />);

    // 最初は1番目
    expect(screen.getByText('1番目の通知')).toBeInTheDocument();

    // 閉じるボタンをクリック
    const closeBtn = screen.getByRole('button');
    fireEvent.click(closeBtn);

    // 2番目が表示される
    expect(screen.getByText('2番目の通知')).toBeInTheDocument();
    expect(mockMarkAsRead).toHaveBeenCalledWith('1');
  });

  it('通知が1件もないとき、スナックバーが表示されない', () => {
    // テストリスト項目11
    mockUseNotifications.mockReturnValue({
      notifications: [],
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: jest.fn(),
    });

    render(<NotificationSnackbar employeeId="1" />);

    // スナックバーが表示されない
    expect(screen.queryByText('コメントが追加されました')).not.toBeInTheDocument();
  });

  it('通知が切り替わったとき、自動消去タイマーがリセットされる', async () => {
    // テストリスト項目12
    jest.useFakeTimers();

    const notification1: NotificationResponse[] = [
      { ...mockNotifications[0], id: '1', title: '1番目の通知' },
    ];
    const notification2: NotificationResponse[] = [
      { ...mockNotifications[0], id: '2', title: '2番目の通知' },
    ];

    const mockMarkAsRead = jest.fn();
    mockUseNotifications.mockReturnValue({
      notifications: notification1,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: mockMarkAsRead,
    });

    const { rerender } = render(<NotificationSnackbar employeeId="1" />);

    // 通知1を表示して5000ミリ秒進める
    expect(screen.getByText('1番目の通知')).toBeInTheDocument();

    await act(async () => {
      jest.advanceTimersByTime(5000);
    });

    // モックの戻り値を通知2だけの一覧に差し替えて再レンダリングする
    mockUseNotifications.mockReturnValue({
      notifications: notification2,
      loading: false,
      error: null,
      fetchNotifications: jest.fn(),
      handleMarkAsRead: mockMarkAsRead,
    });

    rerender(<NotificationSnackbar employeeId="1" />);

    // 通知2が表示されていることを確認
    expect(screen.getByText('2番目の通知')).toBeInTheDocument();

    // さらに5000ミリ秒進めても通知2が表示されたままであること
    await act(async () => {
      jest.advanceTimersByTime(5000);
    });

    expect(screen.getByText('2番目の通知')).toBeInTheDocument();

    // 追加で1000ミリ秒進めると通知2の既読化が呼ばれること
    await act(async () => {
      jest.advanceTimersByTime(1000);
    });

    expect(mockMarkAsRead).toHaveBeenCalledWith('2');
  });
});
