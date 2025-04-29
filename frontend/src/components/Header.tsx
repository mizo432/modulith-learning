import React from 'react';
import {AppBar, Box, Button, IconButton, Toolbar, Typography} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';
import {useAuth} from '../contexts/AuthContext';
import MenuIcon from '@mui/icons-material/Menu';
import SideMenu from './SideMenu';

const Header: React.FC = () => {
  const {isAuthenticated, username, logout} = useAuth();
  const [menuOpen, setMenuOpen] = React.useState(false);

  const handleMenuOpen = () => {
    setMenuOpen(true);
  };

  const handleMenuClose = () => {
    setMenuOpen(false);
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
            <Typography variant="h6" component="div" sx={{flexGrow: 1}}>
              Modulith Learning
            </Typography>
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

        {/* Side Menu */}
        {isAuthenticated && (
            <SideMenu open={menuOpen} onClose={handleMenuClose}/>
        )}
      </>
  );
};

export default Header;
