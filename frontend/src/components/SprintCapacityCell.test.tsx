import React from 'react';
import { render, screen } from '@testing-library/react';
import SprintCapacityCell from './SprintCapacityCell';
import sprintService, { SprintCapacity } from '../services/sprintService';

jest.mock('../services/sprintService', () => ({
  __esModule: true,
  default: {
    getSprintCapacity: jest.fn(),
  },
}));

const mockGetSprintCapacity = sprintService.getSprintCapacity as jest.Mock;

const capacity = (overrides: Partial<SprintCapacity>): SprintCapacity => ({
  sprintId: '3001',
  businessDays: 10,
  velocityPerBusinessDay: 5,
  assignablePoints: 50,
  assignedPoints: 30,
  overAllocated: false,
  ...overrides,
});

describe('SprintCapacityCell', () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  it('割り当て済みと割り当て可能ポイントを表示する', async () => {
    mockGetSprintCapacity.mockResolvedValue(capacity({}));

    render(<SprintCapacityCell sprintId="3001" />);

    expect(await screen.findByText('30 / 50 SP')).toBeInTheDocument();
    expect(screen.queryByText('割り当て可能ポイントを超えています')).not.toBeInTheDocument();
  });

  it('割り当て済みが割り当て可能を超えるとき、警告を表示する', async () => {
    mockGetSprintCapacity.mockResolvedValue(
      capacity({ assignedPoints: 55, overAllocated: true })
    );

    render(<SprintCapacityCell sprintId="3001" />);

    expect(await screen.findByText('55 / 50 SP')).toBeInTheDocument();
    expect(screen.getByText('割り当て可能ポイントを超えています')).toBeInTheDocument();
  });

  it('取得に失敗したとき、-を表示する', async () => {
    mockGetSprintCapacity.mockRejectedValue(new Error('failed'));

    render(<SprintCapacityCell sprintId="3001" />);

    expect(await screen.findByText('-')).toBeInTheDocument();
  });
});
