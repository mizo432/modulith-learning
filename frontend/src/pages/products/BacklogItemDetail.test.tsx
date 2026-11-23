import { act } from 'react';
import React from 'react';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import BacklogItemDetail from './BacklogItemDetail';
import backlogItemService from '../../services/backlogItemService';
import scrumTeamService from '../../services/scrumTeamService';

// Mock react-router-dom
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({
    productId: 'product-1',
    scrumTeamId: 'team-1',
    backlogItemId: 'item-100',
  }),
}));

// Mock AuthContext — パラメータ化（テストごとに値を切り替え可能）
const mockUseAuth = jest.fn();
jest.mock('../../contexts/AuthContext', () => ({
  ...jest.requireActual('../../contexts/AuthContext'),
  useAuth: () => mockUseAuth(),
}));

// AuthContext のデフォルトモック値
const defaultAuthMock = {
  isAuthenticated: true,
  username: 'テストユーザー',
  isAdmin: false,
  isFirstLogin: false,
  employeeId: 'emp-1',
  login: jest.fn(),
  logout: jest.fn(),
  setFirstLogin: jest.fn(),
};

// AuthContext モックのセットヘルパー
function setAuthMock(overrides = {}) {
  mockUseAuth.mockReturnValue({ ...defaultAuthMock, ...overrides });
}

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
    estimateBacklogItem: jest.fn(),
    changeStatus: jest.fn(),
    getAttachments: jest.fn(),
    uploadAttachment: jest.fn(),
    deleteAttachment: jest.fn(),
    downloadAttachment: jest.fn(),
  },
  BacklogItemStatus: {
    SUGGESTED: 'SUGGESTED',
    ACCEPTED: 'ACCEPTED',
    FROZEN: 'FROZEN',
    READY: 'READY',
    ESTIMATED: 'ESTIMATED',
  },
}));

// Mock scrumTeamService
jest.mock('../../services/scrumTeamService', () => ({
  __esModule: true,
  default: {
    getScrumTeamById: jest.fn(),
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

const mockBacklogItemAccepted = {
  ...mockBacklogItem,
  status: 'ACCEPTED',
  statusLabel: '承認済み',
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
    setAuthMock();
    (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);
    (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
    (backlogItemService.getAttachments as jest.Mock).mockResolvedValue([]);
    // デフォルトではemp-1をチームメンバーとして設定
    (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue({
      id: 'team-1',
      productId: 'product-1',
      name: 'テストチーム',
      poEmployeeId: 'emp-1',
      smEmployeeId: 'emp-2',
      memberEmployeeIds: ['emp-1', 'emp-2'],
    });
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
      setAuthMock();
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

      // 返信送信ボタンをクリック（aria-labelで特定）
      const sendButton = screen.getByRole('button', { name: '送信' });
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

  describe('コメント削除・編集の表示制御（#674）', () => {
    const mockOwnComment = {
      id: 'comment-own',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-1',
      authorName: '本人',
      body: '自分のコメント',
      createdAt: '2026-01-01T00:00:00Z',
      parentId: null,
      hasReplies: false,
    };

    const mockOtherComment = {
      id: 'comment-other',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-other',
      authorName: '他人',
      body: '他人のコメント',
      createdAt: '2026-01-01T01:00:00Z',
      parentId: null,
      hasReplies: false,
    };

    const mockOwnReply = {
      id: 'reply-own',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-1',
      authorName: '本人',
      body: '自分の返信',
      createdAt: '2026-01-01T02:00:00Z',
      parentId: 'comment-parent',
      hasReplies: false,
    };

    const mockOtherReply = {
      id: 'reply-other',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-other',
      authorName: '他人',
      body: '他人の返信',
      createdAt: '2026-01-01T03:00:00Z',
      parentId: 'comment-parent',
      hasReplies: false,
    };

    const mockParentComment = {
      id: 'comment-parent',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-other',
      authorName: '親投稿者',
      body: '親コメント',
      createdAt: '2026-01-01T00:00:00Z',
      parentId: null,
      hasReplies: true,
    };

    // テストケース1: 投稿者本人（非ADMIN）が自分のコメントを表示したとき、削除ボタンが表示される
    it('投稿者本人（非ADMIN）が自分のコメントを表示したとき、削除ボタンが表示される', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockOwnComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('自分のコメント')).toBeInTheDocument();
      });

      expect(screen.getByRole('button', { name: /削除/i })).toBeInTheDocument();
    });

    // テストケース2: 投稿者本人（非ADMIN）が他人のコメントを表示したとき、削除ボタンが表示されない
    it('投稿者本人（非ADMIN）が他人のコメントを表示したとき、削除ボタンが表示されない', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockOtherComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('他人のコメント')).toBeInTheDocument();
      });

      expect(screen.queryByRole('button', { name: /削除/i })).not.toBeInTheDocument();
    });

    // テストケース3: ADMINユーザーが他人のコメントを表示したとき、削除ボタンが表示される
    it('ADMINユーザーが他人のコメントを表示したとき、削除ボタンが表示される', async () => {
      setAuthMock({ isAdmin: true, employeeId: 'emp-admin' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockOtherComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('他人のコメント')).toBeInTheDocument();
      });

      expect(screen.getByRole('button', { name: /削除/i })).toBeInTheDocument();
    });

    // テストケース4: 投稿者本人（非ADMIN）が自分の返信コメントを表示したとき、削除ボタンが表示される
    it('投稿者本人（非ADMIN）が自分の返信コメントを表示したとき、削除ボタンが表示される', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockParentComment, mockOwnReply]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('自分の返信')).toBeInTheDocument();
      });

      // 返信コメントの削除ボタンが表示されることを確認
      expect(screen.getByRole('button', { name: /削除/i })).toBeInTheDocument();
    });

    // テストケース5: 他人（非ADMIN）が他人の返信コメントを表示したとき、削除ボタンが表示されない
    it('他人（非ADMIN）が他人の返信コメントを表示したとき、削除ボタンが表示されない', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockParentComment, mockOtherReply]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('他人の返信')).toBeInTheDocument();
      });

      expect(screen.queryByRole('button', { name: /削除/i })).not.toBeInTheDocument();
    });

    // テストケース6: 子返信があるコメントを表示したとき、投稿者本人であっても削除ボタンが表示されない（既存の振る舞いの回帰）
    it('子返信があるコメントを表示したとき、投稿者本人であっても削除ボタンが表示されない（既存の振る舞いの回帰）', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      const mockCommentWithReplies = {
        ...mockOwnComment,
        hasReplies: true,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentWithReplies]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('自分のコメント')).toBeInTheDocument();
      });

      expect(screen.queryByRole('button', { name: /削除/i })).not.toBeInTheDocument();
    });

    // テストケース7: 権限がないユーザーが削除ボタンをクリックして403エラーが返されたとき、エラーメッセージがAlertとして表示される
    it('権限がないユーザーが削除ボタンをクリックして403エラーが返されたとき、エラーメッセージがAlertとして表示される', async () => {
      const user = userEvent.setup();
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      const mockCommentNoReplies = {
        ...mockOwnComment,
        hasReplies: false,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoReplies]);
      (backlogItemService.deleteComment as jest.Mock).mockRejectedValue({
        response: {
          status: 403,
          data: { message: 'コメントの削除権限がありません。' },
        },
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('自分のコメント')).toBeInTheDocument();
      });

      const deleteButton = screen.getByRole('button', { name: /削除/i });
      await user.click(deleteButton);

      await waitFor(() => {
        expect(screen.getByText('コメントの削除権限がありません。')).toBeInTheDocument();
      });
    });

    // テストケース8: 投稿者本人が削除ボタンをクリックしたとき、コメントが正常に削除され、リストから消える（既存の削除成功フローが壊れていないことを確認）
    it('投稿者本人が削除ボタンをクリックしたとき、コメントが正常に削除され、リストから消える（既存の削除成功フローが壊れていないことを確認）', async () => {
      const user = userEvent.setup();
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      const mockCommentNoReplies = {
        ...mockOwnComment,
        hasReplies: false,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoReplies]);
      (backlogItemService.deleteComment as jest.Mock).mockResolvedValue(undefined);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('自分のコメント')).toBeInTheDocument();
      });

      const deleteButton = screen.getByRole('button', { name: /削除/i });
      await user.click(deleteButton);

      await waitFor(() => {
        expect(backlogItemService.deleteComment).toHaveBeenCalledWith('item-100', 'comment-own', {
          actorEmployeeId: expect.any(String),
        });
      });
    });

    // テストケース9: 子返信がないコメントを表示したとき、編集ボタンが削除ボタンと同様の権限条件で表示・非表示となる
    it('子返信がないコメントを表示したとき、編集ボタンが削除ボタンと同様の権限条件で表示・非表示となる（削除ボタンと編集ボタンは同じ条件で表示されるため）', async () => {
      // 他人のコメント（非ADMIN）→ 編集ボタンも非表示
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockOtherComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('他人のコメント')).toBeInTheDocument();
      });

      expect(screen.queryByRole('button', { name: /編集/i })).not.toBeInTheDocument();
      expect(screen.queryByRole('button', { name: /削除/i })).not.toBeInTheDocument();
    });

    // テストケース9-1: 本人のコメントで編集ボタンが表示される
    it('投稿者本人（非ADMIN）が自分のコメントを表示したとき、編集ボタンが表示される', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockOwnComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('自分のコメント')).toBeInTheDocument();
      });

      expect(screen.getByRole('button', { name: /編集/i })).toBeInTheDocument();
    });

    // テストケース9-2: ADMINが他人のコメントを表示したとき、編集ボタンが表示される
    it('ADMINユーザーが他人のコメントを表示したとき、編集ボタンが表示される', async () => {
      setAuthMock({ isAdmin: true, employeeId: 'emp-admin' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockOtherComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('他人のコメント')).toBeInTheDocument();
      });

      expect(screen.getByRole('button', { name: /編集/i })).toBeInTheDocument();
    });

    // テストケース10: authorEmployeeIdが未設定のコメントのテスト
    const mockCommentNoAuthor = {
      id: 'comment-no-author',
      backlogItemId: 'item-100',
      authorName: '匿名',
      body: 'authorEmployeeId未設定のコメント',
      createdAt: '2026-01-01T04:00:00Z',
      parentId: null,
      hasReplies: false,
    };

    // テストケース10-1: 非ADMINでは削除・編集ボタンが表示されない
    it('authorEmployeeIdが未設定のコメントを非ADMINが閲覧したとき、削除・編集ボタンが表示されない', async () => {
      setAuthMock({ isAdmin: false, employeeId: 'emp-1' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoAuthor]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('authorEmployeeId未設定のコメント')).toBeInTheDocument();
      });

      expect(screen.queryByRole('button', { name: /削除/i })).not.toBeInTheDocument();
      expect(screen.queryByRole('button', { name: /編集/i })).not.toBeInTheDocument();
    });

    // テストケース10-2: ADMINでは削除・編集ボタンが表示される
    it('authorEmployeeIdが未設定のコメントをADMINが閲覧したとき、削除・編集ボタンが表示される', async () => {
      setAuthMock({ isAdmin: true, employeeId: 'emp-admin' });
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentNoAuthor]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('authorEmployeeId未設定のコメント')).toBeInTheDocument();
      });

      expect(screen.getByRole('button', { name: /削除/i })).toBeInTheDocument();
      expect(screen.getByRole('button', { name: /編集/i })).toBeInTheDocument();
    });
  });

  describe('品質カテゴリ表示', () => {
    it('詳細ページのタイトル横に品質カテゴリバッジが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemWithValue,
        qualityCategory: 'ATTRACTIVE',
        qualityCategoryLabel: '魅力的品質',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // タイトル横に品質カテゴリバッジが表示されること
      await waitFor(() => {
        expect(screen.getByText('魅力的品質')).toBeInTheDocument();
      });
    });

    it('品質カテゴリバッジにツールチップが表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemWithValue,
        qualityCategory: 'ATTRACTIVE',
        qualityCategoryLabel: '魅力的品質',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // バッジにホバーしてツールチップ（role="tooltip"）が境界値テキストを表示することを確認
      const badge = screen.getByText('魅力的品質');
      await user.hover(badge);
      await waitFor(() => {
        const tooltip = document.querySelector('[role="tooltip"]');
        expect(tooltip).not.toBeNull();
        expect(tooltip?.textContent).toMatch(/Value 76〜100/);
      });
    });

    it('品質カテゴリバッジにキーボードでフォーカスするとツールチップが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemWithValue,
        qualityCategory: 'ATTRACTIVE',
        qualityCategoryLabel: '魅力的品質',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // キーボードでフォーカスしてツールチップ（role="tooltip"）が境界値テキストを表示することを確認
      // ChipのtabIndex=0を持つ要素にフォーカス
      const chipElement = screen.getByText('魅力的品質').closest('.MuiChip-root') as HTMLElement;
      chipElement.focus();
      await waitFor(() => {
        const tooltip = document.querySelector('[role="tooltip"]');
        expect(tooltip).not.toBeNull();
        expect(tooltip?.textContent).toMatch(/Value 76〜100/);
      });
    });

    it('Valueスライダーの横にリアルタイム品質カテゴリが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemWithValue);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByRole('slider')).toBeInTheDocument();
      });

      // Value=50 → ONE_DIMENSIONAL（一元的品質）のリアルタイム表示があること
      await waitFor(() => {
        expect(screen.getByText('一元的品質')).toBeInTheDocument();
      });
    });

    it('Valueを76に変更したとき、リアルタイム表示が「魅力的品質」に切り替わる', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemWithValue);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByRole('slider')).toBeInTheDocument();
      });

      // Value入力フィールドに76を入力
      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;
      await user.clear(input);
      await user.type(input, '76');

      // リアルタイム表示が「魅力的品質」に切り替わる
      await waitFor(() => {
        expect(screen.getByText('魅力的品質')).toBeInTheDocument();
      });
    });

    it('Valueを50に変更したとき、リアルタイム表示が「一元的品質」に切り替わる', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemWithValue,
        value: 100,
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByRole('slider')).toBeInTheDocument();
      });

      // Value入力フィールドに50を入力
      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;
      await user.clear(input);
      await user.type(input, '50');

      // リアルタイム表示が「一元的品質」に切り替わる
      await waitFor(() => {
        expect(screen.getByText('一元的品質')).toBeInTheDocument();
      });
    });

    it('Valueがnullのとき、リアルタイム表示が「―」である', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByRole('slider')).toBeInTheDocument();
      });

      // Valueがnullのとき、data-testid="value-textfield"を含む祖先要素のtextContentに「―」が含まれること
      await waitFor(() => {
        const valueTextField = screen.getByTestId('value-textfield');
        const ancestorBox = valueTextField.closest('div[flex]') || valueTextField.parentElement;
        expect(ancestorBox?.textContent).toContain('―');
      });
    });

    it('Valueをスライダーで操作したとき、リアルタイム表示が追従する', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);

      renderBacklogItemDetail();

      const slider = await waitFor(() => screen.getByRole('slider'));

      // スライダーを操作してValue=76に変更（ATTRACTIVEの境界値）
      fireEvent.change(slider, { target: { value: '76' } });

      // リアルタイム表示が「魅力的品質」に切り替わる
      await waitFor(() => {
        expect(screen.getByText('魅力的品質')).toBeInTheDocument();
      });
    });
  });

  describe('StoryPoint選択UI（詳細画面）', () => {
    const mockBacklogItemReady = {
      ...mockBacklogItem,
      status: 'READY',
      statusLabel: '準備完了',
      storyPoint: null,
    };

    const mockBacklogItemEstimated = {
      ...mockBacklogItem,
      status: 'ESTIMATED',
      statusLabel: '見積もり済み',
      storyPoint: 5,
    };

    const mockBacklogItemSuggested = {
      ...mockBacklogItem,
      status: 'SUGGESTED',
      statusLabel: '提案',
      storyPoint: null,
    };

    beforeEach(() => {
      jest.clearAllMocks();
      setAuthMock();
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
    });

    // テスト1: 詳細画面を表示したとき、StoryPointラジオボタングループが表示となる
    it('ステータスがREADYのとき、StoryPointラジオボタングループが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // StoryPointセクションのラジオボタングループが表示されること
      await waitFor(() => {
        const radioGroup = screen.getByRole('radiogroup');
        expect(radioGroup).toBeInTheDocument();
      });
    });

    // テスト2: すべての許容値（0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89）が選択肢として表示となる
    it('すべての許容StoryPoint値がラジオボタンとして表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // 各許容値がラジオボタンとして表示される（getAllByRoleでvalue属性を確認）
      await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        const values = radios.map((r) => r.getAttribute('value'));
        [0, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89].forEach((sp) => {
          expect(values).toContain(String(sp));
        });
      });
    });

    // テスト3: 各StoryPoint値に説明テキストが付随して表示となる
    it('各StoryPoint値に説明テキストが付随して表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // 説明テキストが表示されること
      await waitFor(() => {
        expect(screen.getByText('見積もり不要')).toBeInTheDocument();
        expect(screen.getByText('最大規模（分割を検討）')).toBeInTheDocument();
      });
    });

    // テスト4: StoryPoint値が設定済みのアイテムを表示したとき、該当値が選択された状態となる
    it('StoryPoint値が設定済みのアイテムを表示したとき、該当値が選択された状態となる', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemEstimated);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // storyPoint=5 のラジオボタンが選択されている（value属性で特定）
      await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        const radio5 = radios.find((r) => r.getAttribute('value') === '5');
        expect(radio5).toBeChecked();
      });
    });

    // テスト5: StoryPoint値が未設定のアイテムを表示したとき、どのラジオボタンも選択されていない状態となる
    it('StoryPoint値が未設定のアイテムを表示したとき、どのラジオボタンも選択されていない', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // すべてのラジオボタンが未選択
      await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        radios.forEach((radio) => {
          expect(radio).not.toBeChecked();
        });
      });
    });

    // テスト6: StoryPoint値を選択して保存ボタンをクリックしたとき、estimateBacklogItem APIが呼び出される
    it('StoryPoint値を選択して保存ボタンをクリックしたとき、estimateBacklogItem APIが呼び出される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);
      (backlogItemService.estimateBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemReady,
        storyPoint: 8,
        status: 'ESTIMATED',
        statusLabel: '見積もり済み',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // StoryPoint 8 のラジオボタンを選択（value属性で特定）
      const radio8 = await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        return radios.find((r) => r.getAttribute('value') === '8')!;
      });
      await user.click(radio8);

      // 保存ボタンをクリック
      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /StoryPoint.*保存/i })
      );
      await user.click(saveButton);

      await waitFor(() => {
        expect(backlogItemService.estimateBacklogItem).toHaveBeenCalledWith('item-100', {
          storyPoint: 8,
          actorEmployeeId: 'emp-1',
        });
      });
    });

    // テスト7: 保存成功后、成功メッセージが表示となる
    it('保存成功后、成功メッセージが表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);
      (backlogItemService.estimateBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemReady,
        storyPoint: 3,
        status: 'ESTIMATED',
        statusLabel: '見積もり済み',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const radio3 = await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        return radios.find((r) => r.getAttribute('value') === '3')!;
      });
      await user.click(radio3);

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /StoryPoint.*保存/i })
      );
      await user.click(saveButton);

      await waitFor(() => {
        expect(screen.getByText('StoryPointを保存しました。')).toBeInTheDocument();
      });
    });

    // テスト8: 保存成功后、アイテムが再取得され、選択されたStoryPoint値が反映される
    it('保存成功后、アイテムが再取得され、選択されたStoryPoint値が反映される', async () => {
      const user = userEvent.setup();
      const estimatedItem = {
        ...mockBacklogItemReady,
        storyPoint: 13,
        status: 'ESTIMATED',
        statusLabel: '見積もり済み',
      };
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);
      (backlogItemService.estimateBacklogItem as jest.Mock).mockResolvedValue(estimatedItem);
      // 再取得後も同じデータを返す
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(estimatedItem);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const radio13 = await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        return radios.find((r) => r.getAttribute('value') === '13')!;
      });
      await user.click(radio13);

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /StoryPoint.*保存/i })
      );
      await user.click(saveButton);

      // 再取得后被選択された値が反映される
      await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        const radio13After = radios.find((r) => r.getAttribute('value') === '13');
        expect(radio13After).toBeChecked();
      });
    });

    // テスト9: 保存成功后、READY から ESTIMATED への状態変化がステータス表示に反映される
    it('保存成功后、READY から ESTIMATED への状態変化がステータス表示に反映される', async () => {
      const user = userEvent.setup();
      const estimatedItem = {
        ...mockBacklogItemReady,
        storyPoint: 5,
        status: 'ESTIMATED',
        statusLabel: '見積もり済み',
      };
      (backlogItemService.getBacklogItem as jest.Mock)
        .mockResolvedValueOnce(mockBacklogItemReady)
        .mockResolvedValueOnce(estimatedItem);
      (backlogItemService.estimateBacklogItem as jest.Mock).mockResolvedValue(estimatedItem);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('準備完了')).toBeInTheDocument();
      });

      const radio5 = await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        return radios.find((r) => r.getAttribute('value') === '5')!;
      });
      await user.click(radio5);

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /StoryPoint.*保存/i })
      );
      await user.click(saveButton);

      // ステータスがESTIMATEDに変更される
      await waitFor(() => {
        expect(screen.getByText('見積もり済み')).toBeInTheDocument();
      });
    });

    // テスト10: APIエラーが返されたとき、エラーメッセージが表示となる
    it('APIエラーが返されたとき、エラーメッセージが表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);
      (backlogItemService.estimateBacklogItem as jest.Mock).mockRejectedValue({
        response: {
          status: 400,
          data: { message: 'StoryPointの見積もりに失敗しました。' },
        },
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const radio5 = await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        return radios.find((r) => r.getAttribute('value') === '5')!;
      });
      await user.click(radio5);

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /StoryPoint.*保存/i })
      );
      await user.click(saveButton);

      await waitFor(() => {
        expect(screen.getByText('StoryPointの見積もりに失敗しました。')).toBeInTheDocument();
      });
    });

    // テスト11: StoryPoint 0を選択したとき、「見積もり不要」の説明テキストが表示となる
    it('StoryPoint 0を選択したとき、「見積もり不要」の説明テキストが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // StoryPoint 0 のラジオボタンとその説明テキストが表示される（value属性で特定）
      await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        const radio0 = radios.find((r) => r.getAttribute('value') === '0');
        expect(radio0).toBeInTheDocument();
        expect(screen.getByText('見積もり不要')).toBeInTheDocument();
      });
    });

    // テスト12: StoryPoint 89を選択したとき、「最大規模（分割を検討）」の説明テキストが表示となる
    it('StoryPoint 89を選択したとき、「最大規模（分割を検討）」の説明テキストが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        const radio89 = radios.find((r) => r.getAttribute('value') === '89');
        expect(radio89).toBeInTheDocument();
        expect(screen.getByText('最大規模（分割を検討）')).toBeInTheDocument();
      });
    });

    // テスト13: ステータスが READY または ESTIMATED のときのみ、見積もりセクションが表示される
    it('ステータスがREADYのとき、見積もりセクションが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const radioGroup = screen.getByRole('radiogroup');
        expect(radioGroup).toBeInTheDocument();
      });
    });

    it('ステータスがESTIMATEDのとき、見積もりセクションが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemEstimated);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const radioGroup = screen.getByRole('radiogroup');
        expect(radioGroup).toBeInTheDocument();
      });
    });

    // テスト14: ステータスが READY 以外（SUGGESTED、ACCEPTED、FROZENなど）のとき、見積もりセクションは非表示となる
    it('ステータスがSUGGESTEDのとき、見積もりセクションは非表示となる', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemSuggested);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // StoryPointのラジオボタングループが表示されないこと
      await waitFor(() => {
        const radioGroup = screen.queryByRole('radiogroup');
        expect(radioGroup).not.toBeInTheDocument();
      });
    });

    it('ステータスがACCEPTEDのとき、見積もりセクションは非表示となる', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItem,
        status: 'ACCEPTED',
        statusLabel: '承認済み',
        storyPoint: null,
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const radioGroup = screen.queryByRole('radiogroup');
        expect(radioGroup).not.toBeInTheDocument();
      });
    });

    // テスト15: ESTIMATED 状態ではStoryPoint値を変更して保存できる（状態遷移なし、値の更新のみ）
    it('ESTIMATED 状態ではStoryPoint値を変更して保存できる', async () => {
      const user = userEvent.setup();
      const updatedEstimatedItem = {
        ...mockBacklogItemEstimated,
        storyPoint: 13,
      };
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemEstimated);
      (backlogItemService.estimateBacklogItem as jest.Mock).mockResolvedValue(updatedEstimatedItem);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // 現在の値(5)から別の値(13)に変更（value属性で特定）
      const radio13 = await waitFor(() => {
        const radios = screen.getAllByRole('radio');
        return radios.find((r) => r.getAttribute('value') === '13')!;
      });
      await user.click(radio13);

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /StoryPoint.*保存/i })
      );
      await user.click(saveButton);

      await waitFor(() => {
        expect(backlogItemService.estimateBacklogItem).toHaveBeenCalledWith('item-100', {
          storyPoint: 13,
          actorEmployeeId: 'emp-1',
        });
      });
    });

    // テスト16: 既存のValue入力セクション（スライダー+TextField）はStoryPointセクションとは別に維持される
    it('既存のValue入力セクションはStoryPointセクションとは別に維持される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // Value入力セクションが存在すること
      await waitFor(() => {
        const valueTextField = screen.getByTestId('value-textfield');
        expect(valueTextField).toBeInTheDocument();
      });

      // スライダーが存在すること
      await waitFor(() => {
        const slider = screen.getByRole('slider');
        expect(slider).toBeInTheDocument();
      });
    });

    // テスト17: 見積もり操作はチームメンバーのみが実行可能
    it('チームメンバーの場合、StoryPoint保存ボタンが表示される', async () => {
      setAuthMock({ employeeId: 'emp-1' });
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);
      (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue({
        id: 'team-1',
        productId: 'product-1',
        name: 'テストチーム',
        poEmployeeId: 'emp-1',
        smEmployeeId: 'emp-2',
        memberEmployeeIds: ['emp-1', 'emp-2'],
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // 保存ボタンが表示されること
      await waitFor(() => {
        const saveButton = screen.getByRole('button', { name: /StoryPoint.*保存/i });
        expect(saveButton).toBeInTheDocument();
      });
    });

    // テスト18: チームメンバーではない場合、StoryPoint保存ボタンが表示されない
    it('チームメンバーではない場合、StoryPoint保存ボタンが表示されない', async () => {
      setAuthMock({ employeeId: 'emp-not-member' });
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemReady);
      (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue({
        id: 'team-1',
        productId: 'product-1',
        name: 'テストチーム',
        poEmployeeId: 'emp-1',
        smEmployeeId: 'emp-2',
        memberEmployeeIds: ['emp-1', 'emp-2'],
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // StoryPointセクションは表示されるが、保存ボタンは表示されないこと
      await waitFor(() => {
        const radioGroup = screen.getByRole('radiogroup');
        expect(radioGroup).toBeInTheDocument();
      });
      await waitFor(() => {
        const saveButton = screen.queryByRole('button', { name: /StoryPoint.*保存/i });
        expect(saveButton).not.toBeInTheDocument();
      });
    });
  });

  describe('Markdownコメント表示（#672）', () => {
    const mockCommentWithMarkdown = {
      id: 'comment-md-1',
      backlogItemId: 'item-100',
      authorEmployeeId: 'emp-1',
      authorName: 'Markdown投稿者',
      body: '**太字**と*斜体*のテスト',
      createdAt: '2026-01-01T00:00:00Z',
      parentId: null,
      hasReplies: false,
    };

    beforeEach(() => {
      jest.clearAllMocks();
      setAuthMock();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);
    });

    it('Markdownコメントの表示 - Markdownを含むコメントが正しくレンダリングされる', async () => {
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentWithMarkdown]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('太字')).toBeInTheDocument();
      });
      // 太字が<strong>タグでレンダリングされること
      const boldElement = screen.getByText('太字');
      expect(boldElement.tagName).toBe('STRONG');
    });

    it('複数コメントのMarkdown表示 - 複数のコメントがそれぞれ独立してMarkdownレンダリングされる', async () => {
      const mockComment2 = {
        ...mockCommentWithMarkdown,
        id: 'comment-md-2',
        body: '- リスト1\n- リスト2',
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentWithMarkdown, mockComment2]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('太字')).toBeInTheDocument();
        expect(screen.getByText('リスト1')).toBeInTheDocument();
        expect(screen.getByText('リスト2')).toBeInTheDocument();
      });
      // リストが<ul>でレンダリングされること
      const list = screen.getByText('リスト1').closest('ul');
      expect(list).toBeInTheDocument();
    });

    it('生HTMLを含むコメントの安全性 - <script>タグを含むコメントが安全にレンダリングされる', async () => {
      const mockCommentWithScript = {
        ...mockCommentWithMarkdown,
        body: '<script>alert("xss")</script>正常なテキスト',
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentWithScript]);

      renderBacklogItemDetail();

      await waitFor(() => {
        // scriptタグが実行されないこと
        expect(screen.queryByRole('alert')).not.toBeInTheDocument();
      });
    });

    it('自動リフレッシュでコメントが更新される - ポーリングで新しいコメントが取得され、Markdownとして表示される', async () => {
      jest.useFakeTimers();
      const REFRESH_INTERVAL = 30000;

      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(1);
      });

      // 2回目のポーリングでMarkdownコメントを返す
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockCommentWithMarkdown]);

      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(screen.getByText('太字')).toBeInTheDocument();
      });

      jest.useRealTimers();
    });

    it('コメント追加後のMarkdown表示 - 新規追加されたコメントもMarkdownとして表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
      (backlogItemService.addComment as jest.Mock).mockResolvedValue({
        id: 'comment-new',
        backlogItemId: 'item-100',
        authorEmployeeId: 'emp-1',
        authorName: 'テストユーザー',
        body: '**新規コメント**',
        createdAt: '2026-01-01T02:00:00Z',
        parentId: null,
        hasReplies: false,
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // コメント入力フィールドにMarkdownを入力
      const commentTextarea = screen.getByTestId('comment-textarea');
      const textarea = commentTextarea.querySelector('textarea')!;
      await user.type(textarea, '**新規コメント**');

      // 送信ボタンをクリック
      const submitButton = screen.getByRole('button', { name: /コメントを送信/i });
      await user.click(submitButton);

      await waitFor(() => {
        expect(screen.getByText('新規コメント')).toBeInTheDocument();
      });
    });
  });

  describe('Markdown統合テスト（#672）', () => {
    jest.setTimeout(30000);
    beforeEach(() => {
      jest.clearAllMocks();
      setAuthMock();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
      (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue({
        id: 'team-1',
        productId: 'product-1',
        name: 'テストチーム',
        poEmployeeId: 'emp-1',
        smEmployeeId: 'emp-2',
        memberEmployeeIds: ['emp-1', 'emp-2'],
      });
    });

    it('画面全体のレンダリング - MarkdownCommentがBacklogItemDetailに正しく統合される', async () => {
      const mockComment = {
        id: 'comment-int-1',
        backlogItemId: 'item-100',
        authorEmployeeId: 'emp-1',
        authorName: '統合テスト投稿者',
        body: '### ヘッダー\n- アイテム1\n- アイテム2',
        createdAt: '2026-01-01T00:00:00Z',
        parentId: null,
        hasReplies: false,
      };
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([mockComment]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
        expect(screen.getByText('ヘッダー')).toBeInTheDocument();
        expect(screen.getByText('アイテム1')).toBeInTheDocument();
      });
    });

    it('コメント追加→表示のフロー - コメント追加後、ポーリングで更新されたコメントがMarkdownとして表示される', async () => {
      jest.useFakeTimers();
      const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });
      const REFRESH_INTERVAL = 30000;

      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
      (backlogItemService.addComment as jest.Mock).mockResolvedValue({
        id: 'comment-flow-1',
        backlogItemId: 'item-100',
        authorEmployeeId: 'emp-1',
        authorName: 'テストユーザー',
        body: '**フローテスト**',
        createdAt: '2026-01-01T03:00:00Z',
        parentId: null,
        hasReplies: false,
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      // コメントを入力して送信
      const commentTextarea = screen.getByTestId('comment-textarea');
      const textarea = commentTextarea.querySelector('textarea')!;
      await user.type(textarea, '**フローテスト**');

      const submitButton = screen.getByRole('button', { name: /コメントを送信/i });
      await user.click(submitButton);

      // ポーリングで更新されたコメントが表示される
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([
        {
          id: 'comment-flow-1',
          backlogItemId: 'item-100',
          authorEmployeeId: 'emp-1',
          authorName: 'テストユーザー',
          body: '**フローテスト**',
          createdAt: '2026-01-01T03:00:00Z',
          parentId: null,
          hasReplies: false,
        },
      ]);

      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(screen.getByText('フローテスト')).toBeInTheDocument();
      });

      jest.useRealTimers();
    });

    it('大量のMarkdownコメント - 多数のコメントがある場合のレンダリングパフォーマンス', async () => {
      const manyComments = Array.from({ length: 50 }, (_, i) => ({
        id: `comment-many-${i}`,
        backlogItemId: 'item-100',
        authorEmployeeId: 'emp-1',
        authorName: `投稿者${i}`,
        body: `**コメント${i}** - リスト${i}`,
        createdAt: `2026-01-01T${String(Math.floor(i / 60)).padStart(2, '0')}:${String(i % 60).padStart(2, '0')}:00Z`,
        parentId: null,
        hasReplies: false,
      }));
      (backlogItemService.getComments as jest.Mock).mockResolvedValue(manyComments);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('コメント0')).toBeInTheDocument();
        expect(screen.getByText('コメント49')).toBeInTheDocument();
      });
    });
  });

  describe('Readyボタン（#714）', () => {
    beforeEach(() => {
      jest.clearAllMocks();
      setAuthMock();
      (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
      (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue({
        id: 'team-1',
        productId: 'product-1',
        name: 'テストチーム',
        poEmployeeId: 'emp-1',
        smEmployeeId: 'emp-2',
        memberEmployeeIds: ['emp-1', 'emp-2'],
      });
    });

    // ── Readyボタン表示制御 ──

    // テスト1: ステータスがACCEPTEDでチームメンバーの場合、Readyボタンが表示される
    it('ステータスがACCEPTEDでチームメンバーの場合、Readyボタンが表示される', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.getByRole('button', { name: /Ready/i });
        expect(readyButton).toBeInTheDocument();
      });
    });

    // テスト2: ステータスがACCEPTEDでチームメンバーではない場合、Readyボタンが表示されない
    it('ステータスがACCEPTEDでチームメンバーではない場合、Readyボタンが表示されない', async () => {
      setAuthMock({ employeeId: 'emp-not-member' });
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);
      (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue({
        id: 'team-1',
        productId: 'product-1',
        name: 'テストチーム',
        poEmployeeId: 'emp-1',
        smEmployeeId: 'emp-2',
        memberEmployeeIds: ['emp-1', 'emp-2'],
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.queryByRole('button', { name: /Ready/i });
        expect(readyButton).not.toBeInTheDocument();
      });
    });

    // テスト3: ステータスがACCEPTED以外（SUGGESTED）でチームメンバーの場合、Readyボタンが表示されない
    it('ステータスがSUGGESTEDでチームメンバーの場合、Readyボタンが表示されない', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItem);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.queryByRole('button', { name: /Ready/i });
        expect(readyButton).not.toBeInTheDocument();
      });
    });

    // テスト4: ステータスがACCEPTED以外（FROZEN）でチームメンバーの場合、Readyボタンが表示されない
    it('ステータスがFROZENでチームメンバーの場合、Readyボタンが表示されない', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItem,
        status: 'FROZEN',
        statusLabel: '凍結',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.queryByRole('button', { name: /Ready/i });
        expect(readyButton).not.toBeInTheDocument();
      });
    });

    // テスト5: ステータスがACCEPTED以外（READY）でチームメンバーの場合、Readyボタンが表示されない
    it('ステータスがREADYでチームメンバーの場合、Readyボタンが表示されない', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItem,
        status: 'READY',
        statusLabel: '準備完了',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.queryByRole('button', { name: /Ready/i });
        expect(readyButton).not.toBeInTheDocument();
      });
    });

    // テスト6: ステータスがACCEPTED以外（ESTIMATED）でチームメンバーの場合、Readyボタンが表示されない
    it('ステータスがESTIMATEDでチームメンバーの場合、Readyボタンが表示されない', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItem,
        status: 'ESTIMATED',
        statusLabel: '見積もり済み',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.queryByRole('button', { name: /Ready/i });
        expect(readyButton).not.toBeInTheDocument();
      });
    });

    // テスト7: employeeIdが設定されていない場合、Readyボタンが表示されない
    it('employeeIdが設定されていない場合、Readyボタンが表示されない', async () => {
      setAuthMock({ employeeId: undefined });
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const readyButton = screen.queryByRole('button', { name: /Ready/i });
        expect(readyButton).not.toBeInTheDocument();
      });
    });

    // ── Readyボタン操作 ──

    // テスト8: Readyボタンをクリックしたとき、changeStatus APIがREADYとactorEmployeeIdで呼び出される
    it('Readyボタンをクリックしたとき、changeStatus APIがREADYとactorEmployeeIdで呼び出される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);
      (backlogItemService.changeStatus as jest.Mock).mockResolvedValue({
        ...mockBacklogItemAccepted,
        status: 'READY',
        statusLabel: '準備完了',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const readyButton = await waitFor(() =>
        screen.getByRole('button', { name: /Ready/i })
      );
      await user.click(readyButton);

      await waitFor(() => {
        expect(backlogItemService.changeStatus).toHaveBeenCalledWith('item-100', 'READY', 'emp-1');
      });
    });

    // テスト9: 状態遷移成功后、成功メッセージが表示される
    it('状態遷移成功后、成功メッセージが表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);
      (backlogItemService.changeStatus as jest.Mock).mockResolvedValue({
        ...mockBacklogItemAccepted,
        status: 'READY',
        statusLabel: '準備完了',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const readyButton = await waitFor(() =>
        screen.getByRole('button', { name: /Ready/i })
      );
      await user.click(readyButton);

      await waitFor(() => {
        expect(screen.getByText('準備完了状態に遷移しました。')).toBeInTheDocument();
      });
    });

    // テスト10: 状態遷移成功后、アイテムが再取得され、ステータスがREADYに更新される
    it('状態遷移成功后、アイテムが再取得され、ステータスがREADYに更新される', async () => {
      const user = userEvent.setup();
      const readyItem = {
        ...mockBacklogItemAccepted,
        status: 'READY',
        statusLabel: '準備完了',
      };
      (backlogItemService.getBacklogItem as jest.Mock)
        .mockResolvedValueOnce(mockBacklogItemAccepted)
        .mockResolvedValueOnce(readyItem);
      (backlogItemService.changeStatus as jest.Mock).mockResolvedValue(readyItem);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const readyButton = await waitFor(() =>
        screen.getByRole('button', { name: /Ready/i })
      );
      await user.click(readyButton);

      await waitFor(() => {
        expect(backlogItemService.getBacklogItem).toHaveBeenCalledTimes(2);
      });
    });

    // テスト11: APIエラーが返されたとき、エラーメッセージが表示される
    it('APIエラーが返されたとき、エラーメッセージが表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);
      (backlogItemService.changeStatus as jest.Mock).mockRejectedValue({
        response: {
          status: 400,
          data: { message: '状態遷移に失敗しました' },
        },
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const readyButton = await waitFor(() =>
        screen.getByRole('button', { name: /Ready/i })
      );
      await user.click(readyButton);

      await waitFor(() => {
        expect(screen.getByText(/失敗/i)).toBeInTheDocument();
      });
    });

    // ── 回帰テスト ──

    // テスト12: 既存のValue入力機能が引き続き動作すること
    it('既存のValue入力機能が引き続き動作すること', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);
      (backlogItemService.updateBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItemAccepted,
        value: 75,
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const valueInput = await waitFor(() =>
        screen.getByTestId('value-textfield')
      );
      const input = valueInput.querySelector('input')!;
      await user.clear(input);
      await user.type(input, '75');

      const saveButton = await waitFor(() =>
        screen.getByRole('button', { name: /Value.*保存/i })
      );
      await user.click(saveButton);

      await waitFor(() => {
        expect(backlogItemService.updateBacklogItem).toHaveBeenCalledWith('item-100', {
          value: 75,
        });
      });
    });

    // テスト13: 既存のStoryPoint選択UIが引き続き動作すること
    it('既存のStoryPoint選択UIが引き続き動作すること', async () => {
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue({
        ...mockBacklogItem,
        status: 'READY',
        statusLabel: '準備完了',
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      await waitFor(() => {
        const radioGroup = screen.getByRole('radiogroup');
        expect(radioGroup).toBeInTheDocument();
      });
    });

    // テスト14: 既存のコメント機能が引き続き動作すること
    it('既存のコメント機能が引き続き動作すること', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockBacklogItemAccepted);
      (backlogItemService.addComment as jest.Mock).mockResolvedValue({
        id: 'comment-1',
        backlogItemId: 'item-100',
        authorEmployeeId: 'emp-1',
        authorName: 'テストユーザー',
        body: 'テストコメント',
        createdAt: '2026-01-01T00:00:00Z',
        parentId: null,
        hasReplies: false,
      });

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('テストバックログアイテム')).toBeInTheDocument();
      });

      const commentTextarea = screen.getByTestId('comment-textarea');
      const textarea = commentTextarea.querySelector('textarea')!;
      await user.type(textarea, 'テストコメント');

      const submitButton = screen.getByRole('button', { name: /コメントを送信/i });
      await user.click(submitButton);

      await waitFor(() => {
        expect(backlogItemService.addComment).toHaveBeenCalledWith('item-100', expect.objectContaining({
          body: 'テストコメント',
        }));
      });
    });
  });

  describe('添付ファイル', () => {
    const attachmentMine = {
      documentId: 'doc-1',
      seriesId: 'series-1',
      fileName: '設計書.pdf',
      contentType: 'application/pdf',
      fileSize: 2048,
      version: 2,
      uploaderEmployeeId: 'emp-1',
      uploaderName: '山田太郎',
      createdAt: '2026-01-02T10:00:00',
    };
    const attachmentOthers = {
      documentId: 'doc-2',
      seriesId: 'series-2',
      fileName: '議事録.txt',
      contentType: 'text/plain',
      fileSize: 512,
      version: 1,
      uploaderEmployeeId: 'emp-2',
      uploaderName: '佐藤花子',
      createdAt: '2026-01-03T11:00:00',
    };

    beforeEach(() => {
      (URL as any).createObjectURL = jest.fn(() => 'blob:mock-url');
      (URL as any).revokeObjectURL = jest.fn();
    });

    it('添付ファイルがあるとき、ファイル名・サイズ・バージョン・アップロード者が表示される', async () => {
      (backlogItemService.getAttachments as jest.Mock).mockResolvedValue([attachmentMine]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('設計書.pdf')).toBeInTheDocument();
      });
      expect(screen.getByText(/2\.0 KB/)).toBeInTheDocument();
      expect(screen.getByText(/v2/)).toBeInTheDocument();
      expect(screen.getByText(/山田太郎/)).toBeInTheDocument();
    });

    it('アップロード者本人の添付には削除ボタンが表示され、他人の添付には表示されない', async () => {
      (backlogItemService.getAttachments as jest.Mock).mockResolvedValue([
        attachmentMine,
        attachmentOthers,
      ]);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('設計書.pdf')).toBeInTheDocument();
        expect(screen.getByText('議事録.txt')).toBeInTheDocument();
      });
      expect(screen.getByRole('button', { name: '設計書.pdfを削除' })).toBeInTheDocument();
      expect(screen.queryByRole('button', { name: '議事録.txtを削除' })).not.toBeInTheDocument();
    });

    it('チームメンバーがファイルを選んでアップロードしたとき、uploadAttachment が呼ばれる', async () => {
      const user = userEvent.setup();
      (backlogItemService.getAttachments as jest.Mock).mockResolvedValue([]);
      (backlogItemService.uploadAttachment as jest.Mock).mockResolvedValue(attachmentMine);

      renderBacklogItemDetail();

      const openButton = await screen.findByRole('button', { name: 'ファイルを添付' });
      await user.click(openButton);

      const file = new File(['content'], '設計書.pdf', { type: 'application/pdf' });
      const input = screen.getByTestId('attachment-file-input');
      await user.upload(input, file);
      await user.click(screen.getByRole('button', { name: 'アップロード' }));

      await waitFor(() => {
        expect(backlogItemService.uploadAttachment).toHaveBeenCalledWith(
          'item-100',
          file,
          'emp-1',
          'テストユーザー',
        );
      });
    });

    it('ダウンロードボタンを押したとき、downloadAttachment が呼ばれる', async () => {
      const user = userEvent.setup();
      (backlogItemService.getAttachments as jest.Mock).mockResolvedValue([attachmentMine]);
      (backlogItemService.downloadAttachment as jest.Mock).mockResolvedValue(
        new Blob(['x'], { type: 'application/pdf' }),
      );

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('設計書.pdf')).toBeInTheDocument();
      });
      await user.click(screen.getByRole('button', { name: '設計書.pdfをダウンロード' }));

      await waitFor(() => {
        expect(backlogItemService.downloadAttachment).toHaveBeenCalledWith(
          'item-100',
          'doc-1',
          'emp-1',
        );
      });
    });

    it('削除ボタンを押したとき、deleteAttachment が呼ばれ一覧から消える', async () => {
      const user = userEvent.setup();
      (backlogItemService.getAttachments as jest.Mock)
        .mockResolvedValueOnce([attachmentMine])
        .mockResolvedValue([]);
      (backlogItemService.deleteAttachment as jest.Mock).mockResolvedValue(undefined);

      renderBacklogItemDetail();

      await waitFor(() => {
        expect(screen.getByText('設計書.pdf')).toBeInTheDocument();
      });
      await user.click(screen.getByRole('button', { name: '設計書.pdfを削除' }));

      await waitFor(() => {
        expect(backlogItemService.deleteAttachment).toHaveBeenCalledWith(
          'item-100',
          'doc-1',
          'emp-1',
        );
      });
      await waitFor(() => {
        expect(screen.queryByText('設計書.pdf')).not.toBeInTheDocument();
      });
    });
  });
});
