import React, {useState} from 'react';
import {AppBar, Box, Button, IconButton, Toolbar, Typography} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';
import {useAuth} from '../contexts/AuthContext';
import MenuIcon from '@mui/icons-material/Menu';
import SideMenu from './SideMenu';
import TabNavigation, {TabType} from './TabNavigation';

const Header: React.FC = () => {
  const {isAuthenticated, username, isAdmin, logout} = useAuth();
  const [menuOpen, setMenuOpen] = useState(false);
  const [selectedTab, setSelectedTab] = useState<TabType>('projects');

  const handleMenuOpen = () => {
    setMenuOpen(true);
  };

  const handleMenuClose = () => {
    setMenuOpen(false);
  };

  const handleTabChange = (tab: TabType) => {
    // Only allow admin users to select the Users tab
    if (tab === 'users' && !isAdmin) {
      return;
    }
    setSelectedTab(tab);
  };

  return (
      <>
        <AppBar position="static">
          <Toolbar>
            {isAuthenticated && (
                <IconButton
                    edge="start"
                    color="inherit"
                    aria-label="menu"
                    onClick={handleMenuOpen}
                    sx={{mr: 2}}
                >
                  <MenuIcon/>
                </IconButton>
            )}
            <Box sx={{flexGrow: 1, display: 'flex', alignItems: 'center'}}>
              <Typography variant="h6" component="div">
                Modulith Learning
              </Typography>
              {process.env.REACT_APP_ENV && process.env.REACT_APP_ENV !== 'production' && (
                  <Typography
                      variant="caption"
                      component="div"
                      sx={{
                        ml: 1,
                        px: 1,
                        py: 0.5,
                        bgcolor: 'rgba(255, 255, 255, 0.2)',
                        borderRadius: 1,
                        textTransform: 'uppercase'
                      }}
                  >
                    {process.env.REACT_APP_ENV}
                  </Typography>
              )}
            </Box>
            <Box>
              {isAuthenticated ? (
                  <>
                    <Typography variant="body1" component="span" sx={{mr: 2}}>
                      Welcome, {username}
                    </Typography>
                    <Button color="inherit" component={RouterLink} to="/">
                      Dashboard
                    </Button>
                    <Button color="inherit" onClick={logout}>
                      Logout
                    </Button>
                  </>
              ) : (
                  <Button color="inherit" component={RouterLink} to="/login">
                    Login
                  </Button>
              )}
            </Box>
          </Toolbar>
        </AppBar>

        {/* Tab Navigation - only show when authenticated */}
        {isAuthenticated && (
            <TabNavigation
                selectedTab={selectedTab}
                onTabChange={handleTabChange}
            />
        )}

        {/* Side Menu */}
        {isAuthenticated && (
            <SideMenu
                open={menuOpen}
                onClose={handleMenuClose}
                selectedTab={selectedTab}
            />
        )}
      </>
  );
};

export default Header;
