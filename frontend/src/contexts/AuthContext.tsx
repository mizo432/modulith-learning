import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';

interface AuthContextType {
  isAuthenticated: boolean;
  username: string;
  isAdmin: boolean;
  isFirstLogin: boolean;
  login: (token: string, username: string, isAdmin: boolean, isFirstLogin?: boolean) => void;
  logout: () => void;
  setFirstLogin: (value: boolean) => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

export const AuthProvider: React.FC<{ children: ReactNode }> = ({children}) => {
  const navigate = useNavigate();
  const [isAuthenticated, setIsAuthenticated] = useState(false);
  const [username, setUsername] = useState('');
  const [isAdmin, setIsAdmin] = useState(false);
  const [isFirstLogin, setIsFirstLogin] = useState(false);

  useEffect(() => {
    // Check authentication status on mount
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    const admin = localStorage.getItem('isAdmin') === 'true';
    const firstLogin = localStorage.getItem('isFirstLogin') === 'true';

    setIsAuthenticated(!!token);
    setUsername(user || '');
    setIsAdmin(admin);
    setIsFirstLogin(firstLogin);
  }, []);

  const login = (token: string, username: string, isAdmin: boolean, isFirstLogin: boolean = false) => {
    localStorage.setItem('token', token);
    localStorage.setItem('user', username);
    localStorage.setItem('isAdmin', isAdmin.toString());
    localStorage.setItem('isFirstLogin', isFirstLogin.toString());
    setIsAuthenticated(true);
    setUsername(username);
    setIsAdmin(isAdmin);
    setIsFirstLogin(isFirstLogin);
  };

  const setFirstLogin = (value: boolean) => {
    localStorage.setItem('isFirstLogin', value.toString());
    setIsFirstLogin(value);
  };

  const logout = () => {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    localStorage.removeItem('isAdmin');
    localStorage.removeItem('isFirstLogin');
    setIsAuthenticated(false);
    setUsername('');
    setIsAdmin(false);
    setIsFirstLogin(false);
    navigate('/login');
  };

  return (
      <AuthContext.Provider
          value={{isAuthenticated, username, isAdmin, isFirstLogin, login, logout, setFirstLogin}}>
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
