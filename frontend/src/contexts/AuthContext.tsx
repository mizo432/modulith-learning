import React, {createContext, ReactNode, useContext, useEffect, useState} from 'react';
import {useNavigate} from 'react-router-dom';

interface AuthContextType {
  isAuthenticated: boolean;
  username: string;
  isAdmin: boolean;
  isFirstLogin: boolean;
  employeeId: string | null;
  login: (token: string, username: string, isAdmin: boolean, isFirstLogin?: boolean, employeeId?: string | null) => void;
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
  const [employeeId, setEmployeeId] = useState<string | null>(null);

  useEffect(() => {
    // Check authentication status on mount
    const token = localStorage.getItem('token');
    const user = localStorage.getItem('user');
    const admin = localStorage.getItem('isAdmin') === 'true';
    const firstLogin = localStorage.getItem('isFirstLogin') === 'true';
    const empId = localStorage.getItem('employeeId');

    setIsAuthenticated(!!token);
    setUsername(user || '');
    setIsAdmin(admin);
    setIsFirstLogin(firstLogin);
    setEmployeeId(empId || null);
  }, []);

  const login = (token: string, username: string, isAdmin: boolean, isFirstLogin: boolean = false, employeeIdParam: string | null = null) => {
    localStorage.setItem('token', token);
    localStorage.setItem('user', username);
    localStorage.setItem('isAdmin', isAdmin.toString());
    localStorage.setItem('isFirstLogin', isFirstLogin.toString());
    if (employeeIdParam !== null && employeeIdParam !== undefined) {
      localStorage.setItem('employeeId', employeeIdParam);
    } else {
      localStorage.removeItem('employeeId');
    }
    setIsAuthenticated(true);
    setUsername(username);
    setIsAdmin(isAdmin);
    setIsFirstLogin(isFirstLogin);
    setEmployeeId(employeeIdParam);
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
    localStorage.removeItem('employeeId');
    setIsAuthenticated(false);
    setUsername('');
    setIsAdmin(false);
    setIsFirstLogin(false);
    setEmployeeId(null);
    navigate('/login');
  };

  return (
      <AuthContext.Provider
          value={{isAuthenticated, username, isAdmin, isFirstLogin, employeeId, login, logout, setFirstLogin}}>
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
