import React from 'react';
import {render, screen} from '@testing-library/react';
import {MemoryRouter} from 'react-router-dom';
import SideMenu from './SideMenu';

const mockUseAuth = jest.fn();
jest.mock('../contexts/AuthContext', () => ({
  useAuth: () => mockUseAuth(),
}));

beforeEach(() => {
  jest.clearAllMocks();
});

test('管理者でログインしているとき、ジョブ手動起動のメニューが表示される', () => {
  mockUseAuth.mockReturnValue({isAuthenticated: true, isAdmin: true});
  render(
      <MemoryRouter>
        <SideMenu open={true} onClose={() => {}} selectedTab="projects"/>
      </MemoryRouter>
  );
  expect(screen.getByText('ジョブ手動起動')).toBeInTheDocument();
});

test('管理者でないとき、ジョブ手動起動のメニューは表示されない', () => {
  mockUseAuth.mockReturnValue({isAuthenticated: true, isAdmin: false});
  render(
      <MemoryRouter>
        <SideMenu open={true} onClose={() => {}} selectedTab="projects"/>
      </MemoryRouter>
  );
  expect(screen.queryByText('ジョブ手動起動')).not.toBeInTheDocument();
});
