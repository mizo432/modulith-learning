import { act, renderHook } from '@testing-library/react';
import { useIsVisible } from './useIsVisible';

describe('useIsVisible', () => {
  beforeEach(() => {
    // visibilityState の初期値を 'visible' に戻す
    Object.defineProperty(document, 'visibilityState', {
      value: 'visible',
      writable: true,
      configurable: true,
    });
  });

  afterEach(() => {
    // クリーンアップ
    Object.defineProperty(document, 'visibilityState', {
      value: 'visible',
      writable: true,
      configurable: true,
    });
  });

  it('初期値が true である', () => {
    const { result } = renderHook(() => useIsVisible());
    expect(result.current.isVisible).toBe(true);
  });

  it('hidden に変更すると false になる', () => {
    const { result } = renderHook(() => useIsVisible());

    // visibilityState を 'hidden' に変更
    Object.defineProperty(document, 'visibilityState', {
      value: 'hidden',
      writable: true,
      configurable: true,
    });

    // visibilitychange イベントを発火
    act(() => {
      document.dispatchEvent(new Event('visibilitychange'));
    });

    expect(result.current.isVisible).toBe(false);
  });

  it('visible に変更すると true になる', () => {
    const { result } = renderHook(() => useIsVisible());

    // まず hidden に
    Object.defineProperty(document, 'visibilityState', {
      value: 'hidden',
      writable: true,
      configurable: true,
    });
    act(() => {
      document.dispatchEvent(new Event('visibilitychange'));
    });
    expect(result.current.isVisible).toBe(false);

    // visible に戻す
    Object.defineProperty(document, 'visibilityState', {
      value: 'visible',
      writable: true,
      configurable: true,
    });
    act(() => {
      document.dispatchEvent(new Event('visibilitychange'));
    });

    expect(result.current.isVisible).toBe(true);
  });

  it('アンマウント時にイベントリスナーが削除される', () => {
    const { unmount } = renderHook(() => useIsVisible());

    // コンソール警告をキャッチ
    const consoleSpy = jest.spyOn(console, 'error').mockImplementation(() => {});

    // アンマウント
    unmount();

    // visibilitychange イベントを発火（リスナーが削除されていれば何もしない）
    act(() => {
      document.dispatchEvent(new Event('visibilitychange'));
    });

    // 警告が発生しないことを確認
    expect(consoleSpy).not.toHaveBeenCalled();

    consoleSpy.mockRestore();
  });
});
