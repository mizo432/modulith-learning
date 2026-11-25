import React from 'react';
import { fireEvent, render, screen, waitFor } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import { MemoryRouter } from 'react-router-dom';
import BacklogItemDetail from './BacklogItemDetail';
import backlogItemService from '../../services/backlogItemService';
import scrumTeamService from '../../services/scrumTeamService';

jest.mock('react-router-dom', () => ({
  ...jest.requireActual('react-router-dom'),
  useParams: () => ({
    productId: 'product-1',
    scrumTeamId: 'team-1',
    backlogItemId: 'item-100',
  }),
}));

const mockUseAuth = jest.fn();
jest.mock('../../contexts/AuthContext', () => ({
  ...jest.requireActual('../../contexts/AuthContext'),
  useAuth: () => mockUseAuth(),
}));

const defaultAuthMock = {
  isAuthenticated: true,
  username: 'テストユーザー',
  isAdmin: false,
  isFirstLogin: false,
  employeeId: 'emp-po',
  login: jest.fn(),
  logout: jest.fn(),
  setFirstLogin: jest.fn(),
};

function setAuthMock(overrides = {}) {
  mockUseAuth.mockReturnValue({ ...defaultAuthMock, ...overrides });
}

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
    getAssignableSprints: jest.fn(),
    assignSprint: jest.fn(),
    unassignSprint: jest.fn(),
    getAttachments: jest.fn(),
    uploadAttachment: jest.fn(),
    deleteAttachment: jest.fn(),
    downloadAttachment: jest.fn(),
    getDependencies: jest.fn(),
    addDependency: jest.fn(),
    removeDependency: jest.fn(),
    getBacklogItemsByTeam: jest.fn(),
  },
}));

jest.mock('../../services/scrumTeamService', () => ({
  __esModule: true,
  default: {
    getScrumTeamById: jest.fn(),
  },
}));

jest.mock('../../services/sprintService', () => ({
  __esModule: true,
  default: {
    getSprintCapacity: jest.fn(),
  },
}));

jest.mock('../../contexts/ToastContext', () => ({
  useToast: () => ({ showToast: jest.fn() }),
}));

const mockItem = {
  id: 'item-100',
  scrumTeamId: 'team-1',
  title: '対象アイテム',
  description: '説明',
  status: 'ACCEPTED',
  statusLabel: '承認済み',
  priority: 1,
  creatorName: '作成者',
  createdAt: '2026-01-01T00:00:00Z',
  updatedAt: '2026-01-01T01:00:00Z',
  value: 50,
};

const mockTeam = {
  id: 'team-1',
  productId: 'product-1',
  name: 'テストチーム',
  poEmployeeId: 'emp-po',
  poName: 'PO',
  smEmployeeId: 'emp-sm',
  smName: 'SM',
  memberEmployeeIds: ['emp-po', 'emp-sm', 'emp-1'],
};

const mockDependencies = {
  backlogItemId: 'item-100',
  dependencies: [
    {
      backlogItemId: 'item-200',
      title: '依存先アイテム',
      status: 'READY',
      statusLabel: '準備完了',
      satisfied: false,
    },
  ],
  dependents: [
    {
      backlogItemId: 'item-300',
      title: '依存元アイテム',
      status: 'ESTIMATED',
      statusLabel: '見積もり済み',
      satisfied: false,
    },
  ],
  dependenciesSatisfied: false,
  unmetDependencyCount: 1,
};

const mockTeamItems = [
  mockItem,
  {
    id: 'item-400',
    scrumTeamId: 'team-1',
    title: '候補アイテム',
    status: 'ACCEPTED',
    statusLabel: '承認済み',
    creatorName: '作成者',
    createdAt: '2026-01-02T00:00:00Z',
  },
];

function setupMocks() {
  (backlogItemService.getBacklogItem as jest.Mock).mockResolvedValue(mockItem);
  (backlogItemService.getComments as jest.Mock).mockResolvedValue([]);
  (backlogItemService.getAttachments as jest.Mock).mockResolvedValue([]);
  (backlogItemService.getAssignableSprints as jest.Mock).mockResolvedValue([]);
  (backlogItemService.getDependencies as jest.Mock).mockResolvedValue(mockDependencies);
  (backlogItemService.getBacklogItemsByTeam as jest.Mock).mockResolvedValue(mockTeamItems);
  (scrumTeamService.getScrumTeamById as jest.Mock).mockResolvedValue(mockTeam);
}

function renderDetail() {
  return render(
    <MemoryRouter>
      <BacklogItemDetail />
    </MemoryRouter>,
  );
}

describe('BacklogItemDetail 依存関係セクション（Issue #542）', () => {
  beforeEach(() => {
    jest.clearAllMocks();
    setAuthMock();
    setupMocks();
  });

  test('詳細画面を開いたとき、依存先と依存元が状態バッジつきでリスト表示される', async () => {
    renderDetail();
    expect(await screen.findByText('依存先アイテム')).toBeInTheDocument();
    expect(screen.getByText('依存元アイテム')).toBeInTheDocument();
    expect(screen.getByText('準備完了')).toBeInTheDocument();
    expect(screen.getByText('見積もり済み')).toBeInTheDocument();
  });

  test('依存先に未完了があるとき、着手できない旨の警告が表示される', async () => {
    renderDetail();
    expect(
      await screen.findByText('依存先が完了していないため、このアイテムには着手できません。'),
    ).toBeInTheDocument();
  });

  test('POで詳細画面を開いたとき、依存の追加・削除UIが表示される', async () => {
    renderDetail();
    expect(await screen.findByTestId('dependency-select')).toBeInTheDocument();
    expect(screen.getByRole('button', { name: '依存を追加' })).toBeInTheDocument();
    expect(screen.getByTestId('remove-dependency-item-200')).toBeInTheDocument();
  });

  test('POでもSMでもないメンバーで詳細画面を開いたとき、追加・削除UIが表示されない', async () => {
    setAuthMock({ employeeId: 'emp-1' });
    renderDetail();
    expect(await screen.findByText('依存先アイテム')).toBeInTheDocument();
    expect(screen.queryByTestId('dependency-select')).not.toBeInTheDocument();
    expect(screen.queryByRole('button', { name: '依存を追加' })).not.toBeInTheDocument();
    expect(screen.queryByTestId('remove-dependency-item-200')).not.toBeInTheDocument();
  });

  test('依存を追加したとき、依存関係が再読み込みされ一覧に反映される', async () => {
    const afterAdd = {
      ...mockDependencies,
      dependencies: [
        ...mockDependencies.dependencies,
        {
          backlogItemId: 'item-400',
          title: '候補アイテム',
          status: 'ACCEPTED',
          statusLabel: '承認済み',
          satisfied: false,
        },
      ],
      unmetDependencyCount: 2,
    };
    (backlogItemService.addDependency as jest.Mock).mockResolvedValue(afterAdd);
    renderDetail();
    const user = userEvent.setup();
    const select = await screen.findByRole('combobox', { name: /依存先/ });
    await user.click(select);
    const option = await screen.findByRole('option', { name: '候補アイテム' });
    await user.click(option);
    await user.click(screen.getByRole('button', { name: '依存を追加' }));
    await waitFor(() =>
      expect(backlogItemService.addDependency).toHaveBeenCalledWith(
        'item-100',
        'item-400',
        'emp-po',
      ),
    );
    expect(await screen.findByText('候補アイテム')).toBeInTheDocument();
  });

  test('依存を削除したとき、依存関係が再読み込みされ一覧から消える', async () => {
    const afterRemove = {
      ...mockDependencies,
      dependencies: [],
      dependenciesSatisfied: true,
      unmetDependencyCount: 0,
    };
    (backlogItemService.removeDependency as jest.Mock).mockResolvedValue(afterRemove);
    renderDetail();
    const removeButton = await screen.findByTestId('remove-dependency-item-200');
    fireEvent.click(removeButton);
    await waitFor(() =>
      expect(backlogItemService.removeDependency).toHaveBeenCalledWith(
        'item-100',
        'item-200',
        'emp-po',
      ),
    );
    await waitFor(() => expect(screen.queryByText('依存先アイテム')).not.toBeInTheDocument());
  });
});
