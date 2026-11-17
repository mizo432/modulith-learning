import React, {useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';
import {Alert, Box, Button, Container, Paper, TextField, Typography} from '@mui/material';
import api from '../services/api';
import {useAuth} from '../contexts/AuthContext';

const Login: React.FC = () => {
  const navigate = useNavigate();
  const {isAuthenticated, login, isFirstLogin: storedFirstLogin} = useAuth();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  // Redirect if already authenticated
  useEffect(() => {
    if (isAuthenticated) {
      if (storedFirstLogin) {
        navigate('/change-password');
      } else {
        navigate('/');
      }
    }
  }, [isAuthenticated, navigate, storedFirstLogin]);

  const handleSubmit = async (e: React.FormEvent) => {
    e.preventDefault();
    setError('');
    setLoading(true);

    try {
      const response = await api.post('/auth/login', {username, password});

      if (response.data.success) {
        // Use the login function from auth context with firstLogin flag
        const firstLogin = response.data.firstLogin || false;
        login(response.data.token || 'dummy-token', response.data.username, response.data.isAdmin || false, firstLogin);

        // Redirect based on firstLogin status
        if (firstLogin) {
          navigate('/change-password');
        } else {
          navigate('/');
        }
      } else {
        setError(response.data.message || 'Authentication failed');
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'An error occurred during login');
    } finally {
      setLoading(false);
    }
  };

  return (
      <Container maxWidth="sm">
        <Box sx={{mt: 8}}>
          <Paper elevation={3} sx={{p: 4}}>
            <Typography variant="h4" component="h1" align="center" gutterBottom>
              Sign In
            </Typography>

            {error && (
                <Alert severity="error" sx={{mb: 2}}>
                  {error}
                </Alert>
            )}

            <Box component="form" onSubmit={handleSubmit} noValidate>
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  id="username"
                  label="Username"
                  name="username"
                  autoComplete="username"
                  autoFocus
                  value={username}
                  onChange={(e) => setUsername(e.target.value)}
              />
              <TextField
                  margin="normal"
                  required
                  fullWidth
                  name="password"
                  label="Password"
                  type="password"
                  id="password"
                  autoComplete="current-password"
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
              />
              <Button
                  type="submit"
                  fullWidth
                  variant="contained"
                  sx={{mt: 3, mb: 2}}
                  disabled={loading}
              >
                {loading ? 'Signing in...' : 'Sign In'}
              </Button>
            </Box>
          </Paper>
        </Box>
      </Container>
  );
};

export default Login;
