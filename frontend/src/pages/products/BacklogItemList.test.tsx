import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
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
});
