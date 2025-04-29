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
  Dashboard as DashboardIcon,
  Group as TeamIcon
} from '@mui/icons-material';
import {Link as RouterLink} from 'react-router-dom';
import {useAuth} from '../contexts/AuthContext';

interface SideMenuProps {
  open: boolean;
  onClose: () => void;
}

const SideMenu: React.FC<SideMenuProps> = ({open, onClose}) => {
  const {isAuthenticated} = useAuth();

  // Only show menu items if user is authenticated
  const menuItems = isAuthenticated ? (
      <List>
        <ListItem disablePadding>
          <ListItemButton component={RouterLink} to="/" onClick={onClose}>
            <ListItemIcon>
              <DashboardIcon/>
            </ListItemIcon>
            <ListItemText primary="Dashboard"/>
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton component={RouterLink} to="/projects" onClick={onClose}>
            <ListItemIcon>
              <ProjectIcon/>
            </ListItemIcon>
            <ListItemText primary="Projects"/>
          </ListItemButton>
        </ListItem>
        <ListItem disablePadding>
          <ListItemButton component={RouterLink} to="/teams" onClick={onClose}>
            <ListItemIcon>
              <TeamIcon/>
            </ListItemIcon>
            <ListItemText primary="Teams"/>
          </ListItemButton>
        </ListItem>
      </List>
  ) : null;

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
        {menuItems}
      </Drawer>
  );
};

export default SideMenu;
