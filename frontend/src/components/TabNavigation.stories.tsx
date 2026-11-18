import React from 'react';
import type { Meta, StoryObj } from '@storybook/react';
import { BrowserRouter } from 'react-router-dom';
import { AuthProvider } from '../contexts/AuthContext';
import TabNavigation, { TabType } from './TabNavigation';

const Wrapper = ({ children }: { children: React.ReactNode }) => (
  <BrowserRouter>
    <AuthProvider>{children}</AuthProvider>
  </BrowserRouter>
);

const meta = {
  title: 'Components/TabNavigation',
  component: TabNavigation,
  parameters: {
    layout: 'centered',
  },
  decorators: [Wrapper],
  args: {
    selectedTab: 'projects',
  },
} satisfies Meta<typeof TabNavigation>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  args: {
    selectedTab: 'projects',
    onTabChange: () => {},
  },
};

export const ProductsTab: Story = {
  args: {
    selectedTab: 'products',
    onTabChange: () => {},
  },
};

export const TeamsTab: Story = {
  args: {
    selectedTab: 'teams',
    onTabChange: () => {},
  },
};

export const SettingsTab: Story = {
  args: {
    selectedTab: 'settings',
    onTabChange: () => {},
  },
};
