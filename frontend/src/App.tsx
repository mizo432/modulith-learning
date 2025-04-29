import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import {Box, Container, CssBaseline} from '@mui/material';
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import NotFound from './pages/NotFound';
import {AuthProvider, useAuth} from './contexts/AuthContext';

// Protected route component that redirects to login if not authenticated
const ProtectedRoute = ({children}: { children: React.ReactNode }) => {
  const {isAuthenticated} = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login"/>;
  }

  return <>{children}</>;
};

// App content component (inside Router)
const AppContent = () => {
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    // Check authentication status when the app loads
    const checkAuth = async () => {
      // You could make an API call here to validate the token if needed
      setIsLoading(false);
    };

    checkAuth();
  }, []);

  if (isLoading) {
    return <div>Loading...</div>;
  }

  return (
      <AuthProvider>
        <CssBaseline/>
        <Box sx={{display: 'flex', flexDirection: 'column', minHeight: '100vh'}}>
          <Header/>
          <Container component="main" sx={{flexGrow: 1, py: 4}}>
            <Routes>
              <Route path="/login" element={<Login/>}/>
              <Route path="/" element={
                <ProtectedRoute>
                  <Dashboard/>
                </ProtectedRoute>
              }/>
              <Route path="*" element={<NotFound/>}/>
            </Routes>
          </Container>
        </Box>
      </AuthProvider>
  );
};

function App() {
  return (
      <Router>
        <AppContent/>
      </Router>
  );
}

export default App;
