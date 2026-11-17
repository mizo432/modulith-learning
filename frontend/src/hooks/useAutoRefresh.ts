import { useEffect, useRef } from 'react';

export function useAutoRefresh(
  callback: () => void,
  interval: number,
  shouldSkip: boolean,
  isVisible: boolean,
): void {
  const callbackRef = useRef(callback);
  callbackRef.current = callback;

  useEffect(() => {
    if (shouldSkip || !isVisible) {
      return;
    }

    const timer = setInterval(() => {
      try {
        callbackRef.current();
      } catch {
        // Error recovery: continue to next cycle
      }
    }, interval);

    return () => {
      clearInterval(timer);
    };
  }, [interval, shouldSkip, isVisible]);
}
