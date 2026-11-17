import { act, renderHook } from '@testing-library/react';
import { useAutoRefresh } from './useAutoRefresh';

const REFRESH_INTERVAL = 30000; // 30秒

describe('useAutoRefresh', () => {
  beforeAll(() => {
    jest.useFakeTimers();
  });

  afterAll(() => {
    jest.useRealTimers();
  });

  beforeEach(() => {
    jest.clearAllMocks();
    // visibilityState の初期値を 'visible' に戻す
    Object.defineProperty(document, 'visibilityState', {
      value: 'visible',
      writable: true,
      configurable: true,
    });
  });

  afterEach(() => {
    Object.defineProperty(document, 'visibilityState', {
      value: 'visible',
      writable: true,
      configurable: true,
    });
  });

  it('指定間隔でコールバックが実行される', () => {
    const callback = jest.fn();
    const shouldSkip = false;
    const isVisible = true;

    renderHook(() => useAutoRefresh(callback, REFRESH_INTERVAL, shouldSkip, isVisible));

    // 初期実行は行われない（または1回目は即実行しない実装の場合）
    expect(callback).toHaveBeenCalledTimes(0);

    // 30秒経過
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(1);

    // さらに30秒経過
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(2);
  });

  it('shouldSkip が true の場合はスキップされる', () => {
    const callback = jest.fn();

    const { rerender } = renderHook(
      ({ shouldSkip }) => useAutoRefresh(callback, REFRESH_INTERVAL, shouldSkip, true),
      { initialProps: { shouldSkip: false } }
    );

    // 30秒経過 — shouldSkip=false なので実行される
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(1);

    // shouldSkip=true に変更
    rerender({ shouldSkip: true });

    // 30秒経過 — shouldSkip=true なのでスキップされる
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(1);
  });

  it('isVisible が false の場合はスキップされる', () => {
    const callback = jest.fn();

    const { rerender } = renderHook(
      ({ isVisible }) => useAutoRefresh(callback, REFRESH_INTERVAL, false, isVisible),
      { initialProps: { isVisible: true } }
    );

    // 30秒経過 — isVisible=true なので実行される
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(1);

    // isVisible=false に変更（タブが非表示）
    rerender({ isVisible: false });

    // 30秒経過 — isVisible=false なのでスキップされる
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(1);
  });

  it('アンマウント時にインターバルがクリアされる', () => {
    const callback = jest.fn();
    const { unmount } = renderHook(() => useAutoRefresh(callback, REFRESH_INTERVAL, false, true));

    // アンマウント
    unmount();

    // 30秒経過 — アンマウントされているので実行されない
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).not.toHaveBeenCalled();
  });

  it('コールバックがエラーを投げても次の周期は実行される', () => {
    const callback = jest.fn().mockImplementation(() => {
      throw new Error('test error');
    });

    renderHook(() => useAutoRefresh(callback, REFRESH_INTERVAL, false, true));

    // 1周期目 — コールバックがエラーを投げる
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(1);

    // 2周期目 — エラー後も次の周期で実行される
    act(() => {
      jest.advanceTimersByTime(REFRESH_INTERVAL);
    });
    expect(callback).toHaveBeenCalledTimes(2);
  });
});
