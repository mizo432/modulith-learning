import { act } from 'react';
import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import userEvent from '@testing-library/user-event';
import BacklogItemList from './BacklogItemList';
import backlogItemService from '../../services/backlogItemService';
import scrumTeamService from '../../services/scrumTeamService';

// Mock react-router-dom
jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({
    productId: 'product-1',
    scrumTeamId: 'team-1',
  }),
  useNavigate: () => jest.fn(),
}));

// Mock backlogItemService
jest.mock('../../services/backlogItemService', () => ({
  __esModule: true,
  default: {
    getBacklogItemsByTeam: jest.fn(),
    createBacklogItem: jest.fn(),
    changeStatus: jest.fn(),
  },
  BacklogItemStatus: {
    SUGGESTED: 'SUGGESTED',
    ACCEPTED: 'ACCEPTED',
    REJECTED: 'REJECTED',
  },
}));

// Mock scrumTeamService
jest.mock('../../services/scrumTeamService', () => ({
  __esModule: true,
  default: {
    getScrumTeamById: jest.fn(),
  },
}));

const mockTeam = {
  id: 'team-1',
  productId: 'product-1',
  name: 'テストチーム',
  poEmployeeId: 'emp-1',
  smEmployeeId: 'emp-2',
};

const mockItemWithValue = {
  id: 'item-1',
  scrumTeamId: 'team-1',
  title: 'Valueありアイテム',
  description: '説明',
  status: 'SUGGESTED',
  statusLabel: '提案',
  priority: 1,
  creatorName: '作成者A',
  createdAt: '2026-01-01T00:00:00Z',
  value: 50,
};

const mockItemWithoutValue = {
  id: 'item-2',
  scrumTeamId: 'team-1',
  title: 'Valueなしアイテム',
  description: '説明',
  status: 'SUGGESTED',
  statusLabel: '提案',
  priority: 2,
  creatorName: '作成者B',
  createdAt: '2026-01-02T00:00:00Z',
  value: null,
};

const renderBacklogItemList = () => {
  return render(
    <MemoryRouter initialEntries={['/products/product-1/scrum-teams/team-1/backlog-items']}>
      <Routes>
        <Route path="/products/:productId/scrum-teams/:scrumTeamId/backlog-items" element={<BacklogItemList />} />
      </Routes>
    </MemoryRouter>
  );
};

describe('BacklogItemList', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    // localStorage をモック - PO としてログイン
    Object.defineProperty(window, 'localStorage', {
      value: {
        getItem: jest.fn((key) => {
          if (key === 'employeeId') return 'emp-1';
          if (key === 'user') return 'テストユーザー';
          if (key === 'token') return 'test-token';
          return null;
        }),
        setItem: jest.fn(),
        removeItem: jest.fn(),
      },
      writable: true,
    });
    (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue(mockTeam);
  });

  describe('Value列の表示', () => {
    it('Value列が表示される', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueありアイテム')).toBeInTheDocument();
      });

      // Value列のヘッダーまたは値が表示されていることを確認
      await waitFor(() => {
        expect(screen.getByText('50')).toBeInTheDocument();
      });
    });

    it('Valueがnullの場合は「-」表示になる', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithoutValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueなしアイテム')).toBeInTheDocument();
      });

      // null値の「-」表示を確認（優先度の「-」と区別するため、行内に「-」が存在することを確認）
      await waitFor(() => {
        expect(screen.getByText('Valueなしアイテム')).toBeInTheDocument();
      });
    });
  });

  describe('承認ボタンの有効・無効', () => {
    it('value=null のアイテムで承認ボタンが disabled になる', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithoutValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueなしアイテム')).toBeInTheDocument();
      });

      // 承認ボタンがdisabledであることを確認
      const acceptButtons = screen.queryAllByText('承認');
      await waitFor(() => {
        expect(acceptButtons.length).toBeGreaterThan(0);
        const button = acceptButtons[0].closest('button');
        expect(button).toBeDisabled();
      });
    });

    it('valueがあるアイテムで承認ボタンが enabled になる', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueありアイテム')).toBeInTheDocument();
      });

      // 承認ボタンがenabledであることを確認
      const acceptButtons = screen.queryAllByText('承認');
      await waitFor(() => {
        expect(acceptButtons.length).toBeGreaterThan(0);
        const button = acceptButtons[0].closest('button');
        expect(button).not.toBeDisabled();
      });
    });

    it('却下ボタンは value の有無に影響されない', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithValue,
        mockItemWithoutValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueありアイテム')).toBeInTheDocument();
        expect(screen.getByText('Valueなしアイテム')).toBeInTheDocument();
      });

      // 却下ボタンがすべてenabledであることを確認
      const rejectButtons = screen.queryAllByText('却下');
      await waitFor(() => {
        expect(rejectButtons.length).toBeGreaterThan(0);
        rejectButtons.forEach((btn) => {
          const button = btn.closest('button');
          expect(button).not.toBeDisabled();
        });
      });
    });
  });

  describe('Valueセルの色表示', () => {
    it('Valueのあるセルのスタイルに色が適用される', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueありアイテム')).toBeInTheDocument();
      });

      // data-testid でValueセルを取得し、子要素(Box)のstyleを確認
      const valueCell = screen.getByTestId(`value-cell-${mockItemWithValue.id}`);
      expect(valueCell).toBeInTheDocument();
      const box = valueCell.querySelector('span')!;
      const styleAttr = box.getAttribute('style') || '';
      // JSDOMはhsl()をrgb()に変換するため、rgb値で確認
      // value=50 → hue=60 → hsl(60,70%,92%) → 背景色はデフォルトとは異なる
      expect(styleAttr).toMatch(/background-color/);
      expect(styleAttr).toMatch(/color/);
    });

    it('Value=null のセルには色が適用されない', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithoutValue,
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Valueなしアイテム')).toBeInTheDocument();
      });

      // Value=null のセルの子要素(Box)のstyleに色が設定されていないことを確認
      const valueCell = screen.getByTestId(`value-cell-${mockItemWithoutValue.id}`);
      expect(valueCell).toBeInTheDocument();
      const box = valueCell.querySelector('span')!;
      const styleAttr = box.getAttribute('style') || '';
      // 色が設定されていない（undefinedのためstyle属性に含まらない）
      expect(styleAttr).not.toMatch(/background-color/);
      expect(styleAttr).not.toMatch(/color/);
    });
  });

  describe('作成フォームのValue案内文', () => {
    it('作成フォームにValue設定の案内文が表示される', async () => {
      const user = userEvent.setup();
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('アイテムを追加')).toBeInTheDocument();
      });

      const addButton = screen.getByRole('button', { name: /アイテムを追加/ });
      await user.click(addButton);

      await waitFor(() => {
        expect(screen.getByText(/Valueは作成後.*詳細ページで設定/)).toBeInTheDocument();
      });
    });
  });

  describe('既定ソート（Value降順）', () => {
    it('Value降順でアイテムが表示される', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        { ...mockItemWithValue, id: 'item-100', title: 'Value100アイテム', value: 100 },
        { ...mockItemWithValue, id: 'item-50', title: 'Value50アイテム', value: 50 },
        { ...mockItemWithValue, id: 'item-10', title: 'Value10アイテム', value: 10 },
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Value100アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value50アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value10アイテム')).toBeInTheDocument();
      });

      // Value値の表示順序を確認（100 → 50 → 10）
      await waitFor(() => {
        expect(screen.getByText('100')).toBeInTheDocument();
        expect(screen.getByText('50')).toBeInTheDocument();
        expect(screen.getByText('10')).toBeInTheDocument();
      });

      // 行のDOM順序でValue降順であることを確認
      const rows = screen.getAllByRole('row');
      const dataRows = rows.slice(1);
      await waitFor(() => {
        expect(dataRows.length).toBeGreaterThanOrEqual(3);
        expect(dataRows[0].textContent).toContain('Value100アイテム');
        expect(dataRows[1].textContent).toContain('Value50アイテム');
        expect(dataRows[2].textContent).toContain('Value10アイテム');
      });
    });

    it('Valueがnullのアイテムは末尾に表示される（APIがソート済みデータを返す前提）', async () => {
      // APIはValue降順でソート済みデータを返す（nullは末尾）
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        { ...mockItemWithValue, id: 'item-100', title: 'Value100アイテム', value: 100 },
        { ...mockItemWithValue, id: 'item-50', title: 'Value50アイテム', value: 50 },
        { ...mockItemWithoutValue, id: 'item-null', title: 'Valueなしアイテム', value: null },
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Value100アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value50アイテム')).toBeInTheDocument();
        expect(screen.getByText('Valueなしアイテム')).toBeInTheDocument();
      });

      // 行のDOM順序でValueなしアイテムが最後であることを確認
      const rows = screen.getAllByRole('row');
      // ヘッダー行を除くデータ行を確認
      const dataRows = rows.slice(1);
      await waitFor(() => {
        expect(dataRows.length).toBeGreaterThanOrEqual(3);
        // 最後のデータ行にValueなしアイテムが含まれていること
        const lastRow = dataRows[dataRows.length - 1];
        expect(lastRow.textContent).toContain('Valueなしアイテム');
      });
    });

    it('状態フィルタ変更後もソート順が維持される', async () => {
      const user = userEvent.setup();

      const acceptedItems = [
        { ...mockItemWithValue, id: 'item-100', title: 'Value100アイテム', value: 100, status: 'ACCEPTED' as const, statusLabel: '受入済み' },
        { ...mockItemWithValue, id: 'item-50', title: 'Value50アイテム', value: 50, status: 'ACCEPTED' as const, statusLabel: '受入済み' },
      ];

      // 初期データとフィルタ後のデータはどちらもValue降順
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue(acceptedItems);

      renderBacklogItemList();

      // 初期読み込みでValue降順で表示されることを確認
      await waitFor(() => {
        expect(screen.getByText('Value100アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value50アイテム')).toBeInTheDocument();
      });

      // 状態フィルタを変更
      const statusSelect = await waitFor(() =>
        screen.getByRole('combobox', { name: /状態フィルタ/i })
      );
      await user.click(statusSelect);
      const acceptedOption = await waitFor(() =>
        screen.getByRole('option', { name: /承認済み/i })
      );
      await user.click(acceptedOption);

      // フィルタ変更で再読み込みされる
      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledWith('team-1', 'ACCEPTED');
      });

      // フィルタ後もValue降順で表示されていることを確認
      await waitFor(() => {
        expect(screen.getByText('Value100アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value50アイテム')).toBeInTheDocument();
      });

      // 行のDOM順序でValue降順であることを確認
      const rows = screen.getAllByRole('row');
      const dataRows = rows.slice(1);
      await waitFor(() => {
        expect(dataRows.length).toBeGreaterThanOrEqual(2);
        expect(dataRows[0].textContent).toContain('Value100アイテム');
        expect(dataRows[1].textContent).toContain('Value50アイテム');
      });
    });

    it('自動リフレッシュ後もソート順が維持される', async () => {
      jest.useFakeTimers();
      const REFRESH_INTERVAL = 30000;

      // 初期データ（Value降順）
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        { ...mockItemWithValue, id: 'item-100', title: 'Value100アイテム', value: 100 },
        { ...mockItemWithValue, id: 'item-50', title: 'Value50アイテム', value: 50 },
      ]);

      renderBacklogItemList();

      await waitFor(() => {
        expect(screen.getByText('Value100アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value50アイテム')).toBeInTheDocument();
      });

      // リフレッシュ後の戻り値もValue降順
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        { ...mockItemWithValue, id: 'item-100', title: 'Value100アイテム', value: 100 },
        { ...mockItemWithValue, id: 'item-50', title: 'Value50アイテム', value: 50 },
      ]);

      // 30秒経過 — リフレッシュ
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(2);
      });

      // リフレッシュ後もValue降順で表示されていることを確認
      await waitFor(() => {
        expect(screen.getByText('Value100アイテム')).toBeInTheDocument();
        expect(screen.getByText('Value50アイテム')).toBeInTheDocument();
      });

      jest.useRealTimers();
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

    it('自動リフレッシュで getBacklogItemsByTeam が定期的に呼び出される', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([]);

      renderBacklogItemList();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(1);
      });

      // 30秒経過 — 2回目の呼び出し
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(2);
      });

      // さらに30秒経過 — 3回目の呼び出し
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(3);
      });
    });

    it('formOpen=true の場合はリフレッシュがスキップされる', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([]);

      renderBacklogItemList();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(1);
      });

      // 追加フォームを開く
      const addButton = await waitFor(() =>
        screen.getByRole('button', { name: /アイテムを追加/ })
      );
      await user.click(addButton);

      await waitFor(() => {
        expect(screen.getByText(/Valueは作成後.*詳細ページで設定/)).toBeInTheDocument();
      });

      // 30秒経過 — フォームが開いているのでリフレッシュされない
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(1);
      });
    });

    it('confirmTarget が設定されている場合はリフレッシュがスキップされる', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithValue,
      ]);

      renderBacklogItemList();

      // 初期読み込みで1回呼び出される
      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(1);
      });

      // 承認ダイアログを開く（confirmTarget が設定される）
      const acceptButtons = await waitFor(() => {
        const buttons = screen.queryAllByText('承認');
        expect(buttons.length).toBeGreaterThan(0);
        return buttons;
      });
      await user.click(acceptButtons[0]);

      // 確認ダイアログが表示されることを待つ
      await waitFor(() => {
        expect(screen.getByRole('dialog')).toBeInTheDocument();
      });

      // 30秒経過 — 確認ダイアログが開いているのでリフレッシュされない
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(1);
      });
    });

    it('状態フィルタがリフレッシュ後も維持される', async () => {
      (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
        mockItemWithValue,
      ]);

      renderBacklogItemList();

      // 初期読み込みで1回呼び出される（フィルタなし）
      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(1);
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledWith('team-1', undefined);
      });

      // 状態フィルタを変更（MUI Select はネイティブ select ではないため、クリック操作で変更する）
      const statusSelect = await waitFor(() =>
        screen.getByRole('combobox', { name: /状態フィルタ/i })
      );
      await user.click(statusSelect);
      const acceptedOption = await waitFor(() =>
        screen.getByRole('option', { name: /承認済み/i })
      );
      await user.click(acceptedOption);

      // フィルタ変更で再読み込みされる
      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledWith('team-1', 'ACCEPTED');
      });

      const callCountAfterFilter = (backlogItemService.getBacklogItemsByTeam as jest.Mock).mock.calls.length;

      // 30秒経過 — リフレッシュされるが、フィルタは維持されたまま
      act(() => {
        jest.advanceTimersByTime(REFRESH_INTERVAL);
      });

      await waitFor(() => {
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenCalledTimes(callCountAfterFilter + 1);
        // 最後の呼び出しでフィルタが維持されていることを確認
        expect(backlogItemService.getBacklogItemsByTeam).toHaveBeenLastCalledWith('team-1', 'ACCEPTED');
      });
    });
  });
});
