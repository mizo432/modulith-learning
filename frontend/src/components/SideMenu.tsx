import React from 'react';
import {
  Box,
  Divider,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText
} from '@mui/material';
import {
  Assignment as ProjectIcon,
  ChevronLeft as ChevronLeftIcon,
  Group as TeamIcon,
  People as PeopleIcon,
  Settings as SettingsIcon
} from '@mui/icons-material';
import {Link as RouterLink} from 'react-router-dom';
import {useAuth} from '../contexts/AuthContext';
import {TabType} from './TabNavigation';

interface SideMenuProps {
  open: boolean;
  onClose: () => void;
  selectedTab: TabType;
}

const SideMenu: React.FC<SideMenuProps> = ({open, onClose, selectedTab}) => {
  const {isAuthenticated, isAdmin} = useAuth();

  // Render different menu items based on the selected tab
  const renderMenuItems = () => {
    if (!isAuthenticated) return null;

    // Only admin users can access the Users tab
    if (selectedTab === 'users' && !isAdmin) {
      return (
          <List>
            <ListItem>
              <ListItemText primary="You don't have permission to access this page."/>
            </ListItem>
          </List>
      );
    }

    switch (selectedTab) {

      case 'users':
        return (
            <List>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/users/all" onClick={onClose}>
                  <ListItemIcon>
                    <PeopleIcon/>
                  </ListItemIcon>
                  <ListItemText primary="All Users"/>
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/users/add" onClick={onClose}>
                  <ListItemIcon>
                    <PeopleIcon/>
                  </ListItemIcon>
                  <ListItemText primary="Add User"/>
                </ListItemButton>
              </ListItem>
            </List>
        );

      case 'projects':
        return (
            <List>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/products" onClick={onClose}>
                  <ListItemIcon>
                    <ProjectIcon/>
                  </ListItemIcon>
                  <ListItemText primary="製品選択"/>
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/projects/all" onClick={onClose}>
                  <ListItemIcon>
                    <ProjectIcon/>
                  </ListItemIcon>
                  <ListItemText primary="All Projects"/>
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/projects/active" onClick={onClose}>
                  <ListItemIcon>
                    <ProjectIcon/>
                  </ListItemIcon>
                  <ListItemText primary="Active Projects"/>
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/projects/archived" onClick={onClose}>
                  <ListItemIcon>
                    <ProjectIcon/>
                  </ListItemIcon>
                  <ListItemText primary="Archived Projects"/>
                </ListItemButton>
              </ListItem>
            </List>
        );

      case 'teams':
        return (
            <List>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/teams/all" onClick={onClose}>
                  <ListItemIcon>
                    <TeamIcon/>
                  </ListItemIcon>
                  <ListItemText primary="All Teams"/>
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/teams/members" onClick={onClose}>
                  <ListItemIcon>
                    <PeopleIcon/>
                  </ListItemIcon>
                  <ListItemText primary="Team Members"/>
                </ListItemButton>
              </ListItem>
            </List>
        );

      case 'settings':
        return (
            <List>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/settings/profile" onClick={onClose}>
                  <ListItemIcon>
                    <SettingsIcon/>
                  </ListItemIcon>
                  <ListItemText primary="Profile Settings"/>
                </ListItemButton>
              </ListItem>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/settings/preferences" onClick={onClose}>
                  <ListItemIcon>
                    <SettingsIcon/>
                  </ListItemIcon>
                  <ListItemText primary="Preferences"/>
                </ListItemButton>
              </ListItem>
            </List>
        );

      default:
        return (
            <List>
              <ListItem disablePadding>
                <ListItemButton component={RouterLink} to="/projects/all" onClick={onClose}>
                  <ListItemIcon>
                    <ProjectIcon/>
                  </ListItemIcon>
                  <ListItemText primary="All Projects"/>
                </ListItemButton>
              </ListItem>
            </List>
        );
    }
  };

  return (
      <Drawer
          anchor="left"
          open={open}
          onClose={onClose}
          sx={{
            '& .MuiDrawer-paper': {
              width: 240,
              boxSizing: 'border-box',
            },
          }}
      >
        <Box sx={{display: 'flex', alignItems: 'center', justifyContent: 'flex-end', p: 1}}>
          <IconButton onClick={onClose}>
            <ChevronLeftIcon/>
          </IconButton>
        </Box>
        <Divider/>
        {renderMenuItems()}
      </Drawer>
  );
};

export default SideMenu;
