import React, {useEffect, useState} from 'react';
import {BrowserRouter as Router, Navigate, Route, Routes} from 'react-router-dom';
import {Box, Container, CssBaseline} from '@mui/material';
import Header from './components/Header';
import Dashboard from './pages/Dashboard';
import Login from './pages/Login';
import NotFound from './pages/NotFound';
import UserList from './pages/users/UserList';
import UserCreate from './pages/users/UserCreate';
import UserEdit from './pages/users/UserEdit';
import ProductSelection from './pages/ProductSelection';
import {AuthProvider, useAuth} from './contexts/AuthContext';

// Protected route component that redirects to login if not authenticated
const ProtectedRoute = ({children}: { children: React.ReactNode }) => {
  const {isAuthenticated} = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login"/>;
  }

  return <>{children}</>;
};

// Admin route component that redirects to dashboard if not admin
const AdminRoute = ({children}: { children: React.ReactNode }) => {
  const {isAuthenticated, isAdmin} = useAuth();

  if (!isAuthenticated) {
    return <Navigate to="/login"/>;
  }

  if (!isAdmin) {
    return <Navigate to="/"/>;
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
              <Route path="/products" element={
                <ProtectedRoute>
                  <ProductSelection/>
                </ProtectedRoute>
              }/>
              {/* User management routes - admin only */}
              <Route path="/users/all" element={
                <AdminRoute>
                  <UserList/>
                </AdminRoute>
              }/>
              <Route path="/users/add" element={
                <AdminRoute>
                  <UserCreate/>
                </AdminRoute>
              }/>
              <Route path="/users/edit/:id" element={
                <AdminRoute>
                  <UserEdit/>
                </AdminRoute>
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
