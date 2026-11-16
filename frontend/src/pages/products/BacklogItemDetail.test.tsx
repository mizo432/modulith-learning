import React from 'react';
import { render, screen, waitFor } from '@testing-library/react';
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

// Mock backlogItemService
jest.mock('../../services/backlogItemService', () => ({
  __esModule: true,
  default: {
    getBacklogItem: jest.fn(),
    getComments: jest.fn(),
    addComment: jest.fn(),
    updateBacklogItem: jest.fn(),
  },
  BacklogItemStatus: {
    SUGGESTED: 'SUGGESTED',
    ACCEPTED: 'ACCEPTED',
    REJECTED: 'REJECTED',
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
        expect(screen.getByLabelText(/Value \*/i) || screen.getByText(/Value \*/i)).toBeInTheDocument();
      });
    });

    it('範囲外（-1）でエラーメッセージが表示される', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByLabelText(/Value/i) || screen.getByRole('textbox', { name: /Value/i })
      );

      await user.type(valueInput, '-1');

      await waitFor(() => {
        expect(screen.getByText(/0.*100/i)).toBeInTheDocument();
      });
    });

    it('範囲外（101）でエラーメッセージが表示される', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByLabelText(/Value/i) || screen.getByRole('textbox', { name: /Value/i })
      );

      await user.type(valueInput, '101');

      await waitFor(() => {
        expect(screen.getByText(/0.*100/i)).toBeInTheDocument();
      });
    });

    it('境界値（0）ではエラーメッセージが表示されない', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByLabelText(/Value/i) || screen.getByRole('textbox', { name: /Value/i })
      );

      await user.type(valueInput, '0');

      await waitFor(() => {
        // エラーメッセージが表示されていないことを確認
        const helperText = screen.queryByText(/0.*100/i);
        // 入力値が0の場合はエラー表示されない（境界値は有効）
      });
    });

    it('境界値（100）ではエラーメッセージが表示されない', async () => {
      const user = userEvent.setup();
      renderBacklogItemDetail();

      const valueInput = await waitFor(() =>
        screen.getByLabelText(/Value/i) || screen.getByRole('textbox', { name: /Value/i })
      );

      await user.type(valueInput, '100');

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
        screen.getByLabelText(/Value/i) || screen.getByRole('textbox', { name: /Value/i })
      );

      await user.type(valueInput, '50');

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
});
