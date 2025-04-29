import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';

interface AuthContextType {
  isAuthenticated: boolean;
  username: string;
  isAdmin: boolean;
  login: (token: string, username: string, isAdmin: boolean) => void;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({children}) => {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [username, setUsername] = useState('');
  const [isAdmin, setIsAdmin] = useState(false);

  useEffect(() => {
    // Check authentication status on mount
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    const admin = localStorage.getItem('isAdmin') === 'true';

    setIsAuthenticated(!!token);
    setUsername(user || '');
    setIsAdmin(admin);
  }, []);

  const login = (token: string, username: string, isAdmin: boolean) => {
    localStorage.setItem('token', token);
    localStorage.setItem('user', username);
    localStorage.setItem('isAdmin', isAdmin.toString());
    setIsAuthenticated(true);
    setUsername(username);
    setIsAdmin(isAdmin);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('isAdmin');
    setIsAuthenticated(false);
    setUsername('');
    setIsAdmin(false);
    navigate('/login');
  };

  return (
      <AuthContext.Provider value={{isAuthenticated, username, isAdmin, login, logout}}>
        {children}
      </AuthContext.Provider>
  );
};

export const useAuth = (): AuthContextType => {
  const context = useContext(AuthContext);
  if (context === undefined) {
    throw new Error('useAuth must be used within an AuthProvider');
  }
  return context;
};
