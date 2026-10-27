import React from 'react';
import {Box, Tab, Tabs} from '@mui/material';
import {useAuth} from '../contexts/AuthContext';

// Define the tab types
export type TabType = 'projects' | 'teams' | 'settings' | 'users';

interface TabNavigationProps {
  selectedTab: TabType;
  onTabChange: (tab: TabType) => void;
}

const TabNavigation: React.FC<TabNavigationProps> = ({selectedTab, onTabChange}) => {
  const {isAdmin} = useAuth();

  const handleChange = (event: React.SyntheticEvent, newValue: TabType) => {
    onTabChange(newValue);
  };

  return (
      <Box sx={{width: '100%', bgcolor: 'background.paper'}}>
        <Tabs
            value={selectedTab}
            onChange={handleChange}
            centered
            indicatorColor="primary"
            textColor="primary"
        >
          <Tab label="プロジェクト" value="projects"/>
          <Tab label="チーム" value="teams"/>
          <Tab label="設定" value="settings"/>
          {isAdmin && <Tab label="ユーザー" value="users"/>}
        </Tabs>
      </Box>
  );
};

export default TabNavigation;
