import api from './api';

// User type definition
export interface User {
  id: number;
  username: string;
  email: string;
  firstName?: string;
  lastName?: string;
  initials?: string;
  enabled: boolean;
  userType: 'EMPLOYEE' | 'BUSINESS_PARTNER_EMPLOYEE' | 'INDIVIDUAL_BUSINESS_PARTNER';
  roles: Role[];
  createdAt: string;
  updatedAt?: string;
  lastLoginAt?: string;
}

// Role type definition
export interface Role {
  id: number;
  name: string;
  description?: string;
}

// User creation request
export interface UserCreateRequest {
  username: string;
  password: string;
  email: string;
  firstName?: string;
  lastName?: string;
  initials?: string;
  userType?: 'EMPLOYEE' | 'BUSINESS_PARTNER_EMPLOYEE' | 'INDIVIDUAL_BUSINESS_PARTNER';
  roles?: string[];
}

// User update request for admins
export interface UserAdminUpdateRequest {
  username?: string;
  email?: string;
  firstName?: string;
  lastName?: string;
  initials?: string;
  userType?: 'EMPLOYEE' | 'BUSINESS_PARTNER_EMPLOYEE' | 'INDIVIDUAL_BUSINESS_PARTNER';
  enabled?: boolean;
  password?: string;
  roles?: string[];
}

// User service functions
const userService = {
  // Get all users
  getAllUsers: async (): Promise<User[]> => {
    const response = await api.get('/users');
    return response.data;
  },

  // Get user by ID
  getUserById: async (id: number): Promise<User> => {
    const response = await api.get(`/users/${id}`);
    return response.data;
  },

  // Create a new user (admin only)
  createUser: async (userData: UserCreateRequest): Promise<User> => {
    const response = await api.post('/users', userData);
    return response.data;
  },

  // Update a user (admin only)
  updateUser: async (id: number, userData: UserAdminUpdateRequest): Promise<User> => {
    const response = await api.put(`/users/${id}`, userData);
    return response.data;
  },

  // Delete a user (admin only)
  deleteUser: async (id: number): Promise<void> => {
    await api.delete(`/users/${id}`);
  },

  // Add a role to a user (admin only)
  addRoleToUser: async (userId: number, roleName: string): Promise<User> => {
    const response = await api.post(`/users/${userId}/roles/${roleName}`);
    return response.data;
  },

  // Remove a role from a user (admin only)
  removeRoleFromUser: async (userId: number, roleName: string): Promise<User> => {
    const response = await api.delete(`/users/${userId}/roles/${roleName}`);
    return response.data;
  },

  // Update user's own profile
  updateProfile: async (userData: {
    firstName?: string;
    lastName?: string;
    initials?: string;
    email?: string;
  }): Promise<User> => {
    const response = await api.put('/users/profile', userData);
    return response.data;
  }
};

export default userService;
