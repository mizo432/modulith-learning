import React from 'react';
import {AppBar, Box, Button, Toolbar, Typography} from '@mui/material';
import {Link as RouterLink} from 'react-router-dom';
import {useAuth} from '../contexts/AuthContext';

const Header: React.FC = () => {
  const {isAuthenticated, username, logout} = useAuth();

  return (
      <AppBar position="static">
        <Toolbar>
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
  );
};

export default Header;
