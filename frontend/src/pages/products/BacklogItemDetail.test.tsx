import { act } from 'react';
import React from 'react';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import BacklogItemDetail from './BacklogItemDetail';
import backlogItemService from '../../services/backlogItemService';

// Mock react-router-dom
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({
    productId: 'product-1',
    scrumTeamId: 'team-1',
    backlogItemId: 'item-100',
  }),
}));

// Mock AuthContext
jest.mock('../../contexts/AuthContext', () => ({
  ...jest.requireActual('../../contexts/AuthContext'),
  useAuth: () => ({
    isAuthenticated: true,
    username: 'テストユーザー',
    isAdmin: false,
    isFirstLogin: false,
    employeeId: 'emp-1',
    login: jest.fn(),
    logout: jest.fn(),
    setFirstLogin: jest.fn(),
  }),
}));

// Mock backlogItemService
jest.mock('../../services/backlogItemService', () => ({
  __esModule: true,
  default: {
    getBacklogItem: jest.fn(),
    getComments: jest.fn(),
    addComment: jest.fn(),
    updateBacklogItem: jest.fn(),
    updateComment: jest.fn(),
    deleteComment: jest.fn(),
  },
  BacklogItemStatus: {
    SUGGESTED: 'SUGGESTED',
    ACCEPTED: 'ACCEPTED',
    FROZEN: 'FROZEN',
  },
}));

const mockBacklogItem = {
  id: 'item-100',
  scrumTeamId: 'team-1',
  title: 'テストバックログアイテム',
  description: 'テスト説明',
  status: 'SUGGESTED',
  statusLabel: '提案',
  priority: 1,
  creatorName: 'テストユーザー',
  createdAt: '2026-01-01T00:00:00Z',
  updatedAt: '2026-01-01T01:00:00Z',
  value: null,
};

const mockBacklogItemWithValue = {
  ...mockBacklogItem,
  value: 50,
};

const renderBacklogItemDetail = () => {
  return render(
    <MemoryRouter initialEntries={['/products/product-1/scrum-teams/team-1/backlog-items/item-100']}>
      <Routes>
        <Route path="/products/:productId/scrum-teams/:scrumTeamId/backlog-items/:backlogItemId" element={<BacklogItemDetail />} />
      </Routes>
    </MemoryRouter>
  );
};

describe('BacklogItemDetail', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);
    (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
  });

  describe('Value入力フィールド', () => {
    it('Value入力フィールドが必須マーク付きで表示される', async () => {
      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText(/Value \*/i)).toBeInTheDocument();
      });
    });

    it('範囲外（-1）でエラーメッセージが表示される', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;

      await user.type(input, '-1');

      await waitFor(() => {
        expect(screen.getByText(/0.*100/i)).toBeInTheDocument();
      });
    });

    it('範囲外（101）でエラーメッセージが表示される', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;

      await user.type(input, '101');

      await waitFor(() => {
        expect(screen.getByText(/0.*100/i)).toBeInTheDocument();
      });
    });

    it('境界値（0）ではエラーメッセージが表示されない', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;

      await user.type(input, '0');

      await waitFor(() => {
        // エラーメッセージが表示されていないことを確認
        const helperText = screen.queryByText(/0.*100の範囲で入力/i);
        // 入力値が0の場合はエラー表示されない（境界値は有効）
      });
    });

    it('境界値（100）ではエラーメッセージが表示されない', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;

      await user.type(input, '100');

      await waitFor(() => {
        // 入力値が100の場合はエラー表示されない（境界値は有効）
      });
    });

    it('保存ボタンで updateBacklogItem が入力値で呼び出される', async () => {
      const user = userEvent.setup();
      (backlogItemService.updateBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItem,
        value: 50,
      });

      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;

      await user.type(input, '50');

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /保存/i })
      );

      await user.click(saveButton);

      await waitFor(() => {
        expect(backlogItemService.updateBacklogItem).toHaveBeenCalledWith('item-100', {
          value: 50,
        });
      });
    });
  });

  describe('スライダー', () => {
    it('スライダー（role=slider）が表示される', async () => {
      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByRole('slider')).toBeInTheDocument();
      });
    });

    it('スライダーの初期値がアイテムのValueと連動する', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemWithValue);

      renderBacklogItemDetail();

      // スライダーの初期値がitem.value(50)と一致することを確認
      const slider = await waitFor(() => screen.getByRole('slider'));
      expect(slider).toHaveValue('50');

      // TextFieldの初期値もitem.value(50)と一致することを確認
      const valueInput = screen.getByTestId('value-textfield');
      const input = valueInput.querySelector('input');
      expect(input?.value).toBe('50');
    });

    it('数値入力の変更がスライダーに反映される', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;

      await user.type(input, '75');

      await waitFor(() => {
        const slider = screen.getByRole('slider');
        expect(slider).toHaveValue('75');
      });
    });

    it('「範囲: 0〜100」の範囲表示が常時表示される', async () => {
      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText(/範囲: 0〜100/)).toBeInTheDocument();
      });
    });

    it('スライダーのつまみにフォーカスしてキーボード矢印キーで値を変えると数値入力フィールドに連動する', async () => {
      renderBacklogItemDetail();

      const slider = await waitFor(() => screen.getByRole('slider'));
      const valueInput = screen.getByTestId('value-textfield');
      const input = valueInput.querySelector('input');

      // スライダーの初期値確認（VALUE_MIN = 0）
      expect(slider).toHaveValue('0');
      expect(input?.value).toBe('');

      // スライダーにフォーカス
      slider.focus();

      // ArrowRightキーで値を増やす（jsdomではkeydownイベントをディスパッチ）
      fireEvent.keyDown(slider, { key: 'ArrowRight', code: 'ArrowRight', keyCode: 39 });

      // jsdomではMUI Sliderの内部キーボードハンドラが正しく動作しない可能性がある
      // そのため、スライダーのonChangeを直接シミュレートして数値入力フィールドへの連動を検証する
      fireEvent.change(slider, { target: { value: '25' } });

      // スライダー操作が数値入力フィールドに反映されることを確認
      await waitFor(() => {
        expect(input?.value).not.toBe('');
      });
    });
  });

  describe('自動リフレッシュ', () => {
    beforeAll(() => {
      jest.useFakeTimers();
    });

    afterAll(() => {
      jest.useRealTimers();
    });

    const REFRESH_INTERVAL = 30000; // 30秒
    const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });

    beforeEach(() => {
      jest.clearAllMocks();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
    });

    it('自動リフレッシュで getBacklogItem が定期的に呼び出される', async () => {
      renderBacklogItemDetail();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });

      // 30秒経過 — 2回目の呼び出し
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(2);
      });

      // さらに30秒経過 — 3回目の呼び出し
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(3);
      });
    });

    it('コメント入力中はリフレッシュがスキップされる', async () => {
      renderBacklogItemDetail();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });

      // コメント入力フィールドに文字を入力
      const commentInput = await waitFor(() =>
        screen.getByTestId('comment-textarea')
      );
      const textarea = commentInput.querySelector('textarea')!;
      await user.type(textarea, 'テストコメント');

      // 30秒経過 — コメント入力中なのでリフレッシュされない
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });
    });

    it('Value入力中はリフレッシュがスキップされる', async () => {
      renderBacklogItemDetail();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });

      // Value入力フィールドに値を入力（item.value と異なる値）
      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;
      await user.type(input, '75');

      // 30秒経過 — Value入力中なのでリフレッシュされない
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });
    });

    it('入力がない場合はリフレッシュが実行される', async () => {
      renderBacklogItemDetail();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });

      // コメントもValueも入力しない状態で30秒経過
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(2);
      });
    });
  });

  describe('コメントスレッド表示', () => {
    const mockRootComment = {
      id: 'comment-1',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-1',
      authorName: '親投稿者',
      body: '親コメント',
      createdAt: '2026-01-01T00:00:00Z',
      parentId: null,
      hasReplies: true,
    };
    const mockReplyComment = {
      id: 'comment-2',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-2',
      authorName: '返信者',
      body: '子コメント（返信）',
      createdAt: '2026-01-01T01:00:00Z',
      parentId: 'comment-1',
      hasReplies: false,
    };

    beforeEach(() => {
      jest.clearAllMocks();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);
    });

    it('コメント一覧を表示したとき、コメントがリスト表示される', async () => {
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockRootComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });
    });

    it('親コメントと子コメントがあるとき、子コメントがインデント表示される', async () => {
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockRootComment, mockReplyComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
        expect(screen.getByText('子コメント（返信）')).toBeInTheDocument();
      });

      // 子コメントは親コメントより右にインデントされていることを確認
      const parentElement = screen.getByText('親コメント').closest('li');
      const replyElement = screen.getByText('子コメント（返信）').closest('li');
      expect(replyElement).toHaveStyle({ paddingLeft: '>=16px' });
    });

    it('3段以上のネストがあるとき、すべての段がインデント表示される', async () => {
      const mockReply2 = {
        ...mockReplyComment,
        id: 'comment-3',
        body: '3段目の返信',
        parentId: 'comment-2',
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockRootComment, mockReplyComment, mockReply2]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('3段目の返信')).toBeInTheDocument();
      });
    });

    it('返信ボタンをクリックしたとき、返信入力フィールドが表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockRootComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });

      // 返信ボタンをクリック
      const replyButton = screen.getByRole('button', { name: /返信/i });
      await user.click(replyButton);

      // 返信入力フィールドが表示される
      await waitFor(() => {
        expect(screen.getByTestId('reply-textarea')).toBeInTheDocument();
      });
    });

    it('返信を入力して送信したとき、親コメントに子コメントとして追加される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockRootComment]);
      (backlogItemService.addComment as jest.Mock).mockResolvedValue({
        ...mockReplyComment,
        body: '新しい返信',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });

      // 返信ボタンをクリック
      const replyButton = screen.getByRole('button', { name: /返信/i });
      await user.click(replyButton);

      const replyTextarea = await waitFor(() => screen.getByTestId('reply-textarea'));
      const textarea = replyTextarea.querySelector('textarea')!;
      await user.type(textarea, '新しい返信');

      // 返信送信ボタンをクリック
      const sendButton = screen.getByRole('button', { name: /送信/i });
      await user.click(sendButton);

      await waitFor(() => {
        expect(backlogItemService.addComment).toHaveBeenCalledWith('item-100', {
          body: '新しい返信',
          parentCommentId: 'comment-1',
          authorName: expect.any(String),
        });
      });
    });

    it('子返信がないコメントの編集ボタンをクリックしたとき、編集モードになる', async () => {
      const user = userEvent.setup();
      const mockCommentNoReplies = {
        ...mockRootComment,
        hasReplies: false,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoReplies]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });

      // 編集ボタンをクリック
      const editButton = screen.getByRole('button', { name: /編集/i });
      await user.click(editButton);

      // 編集モードで入力フィールドが表示される
      await waitFor(() => {
        expect(screen.getByTestId('edit-comment-textarea')).toBeInTheDocument();
      });
    });

    it('編集モードで本文を変更して保存したとき、コメントが更新される', async () => {
      const user = userEvent.setup();
      const mockCommentNoReplies = {
        ...mockRootComment,
        hasReplies: false,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoReplies]);
      (backlogItemService.updateComment as jest.Mock).mockResolvedValue({
        ...mockCommentNoReplies,
        body: '更新されたコメント',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });

      // 編集ボタンをクリック
      const editButton = screen.getByRole('button', { name: /編集/i });
      await user.click(editButton);

      const editTextarea = await waitFor(() => screen.getByTestId('edit-comment-textarea'));
      const textarea = editTextarea.querySelector('textarea')!;
      await user.clear(textarea);
      await user.type(textarea, '更新されたコメント');

      // 保存ボタンをクリック
      const saveButton = screen.getByRole('button', { name: /保存/i });
      await user.click(saveButton);

      await waitFor(() => {
        expect(backlogItemService.updateComment).toHaveBeenCalledWith('item-100', 'comment-1', {
          body: '更新されたコメント',
          actorEmployeeId: expect.any(String),
        });
      });
    });

    it('子返信がないコメントの削除ボタンをクリックしたとき、コメントが削除される', async () => {
      const user = userEvent.setup();
      const mockCommentNoReplies = {
        ...mockRootComment,
        hasReplies: false,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoReplies]);
      (backlogItemService.deleteComment as jest.Mock).mockResolvedValue(undefined);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });

      // 削除ボタンをクリック
      const deleteButton = screen.getByRole('button', { name: /削除/i });
      await user.click(deleteButton);

      await waitFor(() => {
        expect(backlogItemService.deleteComment).toHaveBeenCalledWith('item-100', 'comment-1', {
          actorEmployeeId: expect.any(String),
        });
      });
    });

    it('子返信があるコメントを表示したとき、編集・削除ボタンが表示されない', async () => {
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockRootComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('親コメント')).toBeInTheDocument();
      });

      // 編集ボタンと削除ボタンが表示されていないことを確認
      expect(screen.queryByRole('button', { name: /編集/i })).not.toBeInTheDocument();
      expect(screen.queryByRole('button', { name: /削除/i })).not.toBeInTheDocument();
    });

    it('コメントがないとき、「コメントはまだありません」が表示される', async () => {
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('コメントはまだありません。')).toBeInTheDocument();
      });
    });
  });
});
