import React from 'react';
import { render, screen, fireEvent, act, within } from '@testing-library/react';
import { ToastProvider, useToast } from './ToastContext';

const Harness: React.FC = () => {
  const { showToast } = useToast();
  return (
    <div>
      <button onClick={() => showToast('メッセージ1', 'warning')}>表示1</button>
      <button onClick={() => showToast('メッセージ2', 'warning')}>表示2</button>
      <button onClick={() => showToast('メッセージ3', 'warning')}>表示3</button>
      <button onClick={() => showToast('メッセージ4', 'warning')}>表示4</button>
    </div>
  );
};

const renderHarness = () =>
  render(
    <ToastProvider>
      <Harness />
    </ToastProvider>
  );

describe('ToastContext', () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.useRealTimers();
  });

  it('showToastしたとき、メッセージがトーストとして表示される', () => {
    renderHarness();
    fireEvent.click(screen.getByText('表示1'));
    expect(screen.getByText('メッセージ1')).toBeInTheDocument();
  });

  it('表示から6秒経過したとき、トーストが自動で消える', () => {
    renderHarness();
    fireEvent.click(screen.getByText('表示1'));
    expect(screen.getByText('メッセージ1')).toBeInTheDocument();
    act(() => {
      jest.advanceTimersByTime(7000);
    });
    expect(screen.queryByText('メッセージ1')).not.toBeInTheDocument();
  });

  it('閉じるボタンを押したとき、トーストが消える', () => {
    renderHarness();
    fireEvent.click(screen.getByText('表示1'));
    const alert = screen.getByRole('alert');
    fireEvent.click(within(alert).getByRole('button'));
    act(() => {
      jest.advanceTimersByTime(1000);
    });
    expect(screen.queryByText('メッセージ1')).not.toBeInTheDocument();
  });

  it('同じメッセージを連続でshowToastしたとき、1件に集約される', () => {
    renderHarness();
    fireEvent.click(screen.getByText('表示1'));
    fireEvent.click(screen.getByText('表示1'));
    fireEvent.click(screen.getByText('表示1'));
    expect(screen.getAllByText('メッセージ1')).toHaveLength(1);
  });

  it('4件目のトーストを表示したとき、同時表示は最大3件で最古のものが押し出される', () => {
    renderHarness();
    fireEvent.click(screen.getByText('表示1'));
    fireEvent.click(screen.getByText('表示2'));
    fireEvent.click(screen.getByText('表示3'));
    fireEvent.click(screen.getByText('表示4'));
    expect(screen.queryByText('メッセージ1')).not.toBeInTheDocument();
    expect(screen.getByText('メッセージ2')).toBeInTheDocument();
    expect(screen.getByText('メッセージ3')).toBeInTheDocument();
    expect(screen.getByText('メッセージ4')).toBeInTheDocument();
  });

  it('severity=warningで表示したとき、警告として表示される', () => {
    renderHarness();
    fireEvent.click(screen.getByText('表示1'));
    const alert = screen.getByRole('alert');
    expect(alert).toHaveClass('MuiAlert-filledWarning');
    expect(within(alert).getByText('メッセージ1')).toBeInTheDocument();
  });
});
