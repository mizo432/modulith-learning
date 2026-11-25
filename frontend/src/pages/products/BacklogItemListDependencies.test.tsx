import React from 'react';
import { render, screen } from '@testing-library/react';
import { MemoryRouter, Route, Routes } from 'react-router-dom';
import BacklogItemList from './BacklogItemList';
import backlogItemService from '../../services/backlogItemService';
import scrumTeamService from '../../services/scrumTeamService';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({
    productId: 'product-1',
    scrumTeamId: 'team-1',
  }),
  useNavigate: () => jest.fn(),
}));

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

jest.mock('../../services/backlogItemService', () => ({
  __esModule: true,
  default: {
    getBacklogItemsByTeam: jest.fn(),
    createBacklogItem: jest.fn(),
    changeStatus: jest.fn(),
    estimateBacklogItem: jest.fn(),
  },
}));

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

const itemWithDependencies = {
  id: 'item-1',
  scrumTeamId: 'team-1',
  title: '依存ありアイテム',
  description: '説明',
  status: 'ACCEPTED',
  statusLabel: '承認済み',
  priority: 1,
  creatorName: '作成者A',
  createdAt: '2026-01-01T00:00:00Z',
  value: 50,
  dependencyCount: 2,
  dependenciesSatisfied: false,
};

const itemWithoutDependencies = {
  id: 'item-2',
  scrumTeamId: 'team-1',
  title: '依存なしアイテム',
  description: '説明',
  status: 'SUGGESTED',
  statusLabel: '提案',
  priority: 2,
  creatorName: '作成者B',
  createdAt: '2026-01-02T00:00:00Z',
  value: 30,
  dependencyCount: 0,
  dependenciesSatisfied: true,
};

describe('BacklogItemList 依存件数バッジ（Issue #542）', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue(mockTeam);
    (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue([
      itemWithDependencies,
      itemWithoutDependencies,
    ]);
  });

  test('一覧画面で依存関係のあるアイテムには依存件数のバッジが表示される', async () => {
    render(
      <MemoryRouter initialEntries={['/products/product-1/scrum-teams/team-1/backlog-items']}>
        <Routes>
          <Route
            path="/products/:productId/scrum-teams/:scrumTeamId/backlog-items"
            element={<BacklogItemList />}
          />
        </Routes>
      </MemoryRouter>,
    );
    expect(await screen.findByText('依存ありアイテム')).toBeInTheDocument();
    const badge = screen.getByTestId('dependency-badge-item-1');
    expect(badge).toBeInTheDocument();
    expect(badge).toHaveTextContent('依存 2');
    expect(screen.queryByTestId('dependency-badge-item-2')).not.toBeInTheDocument();
  });
});
