import api from './api';

// Project type definitions
export interface Project {
  projectId: string;
  name: string;
  description?: string;
  status: 'ACTIVE' | 'ARCHIVED';
  projectManager: {
    id: string;
    name: string;
  };
  product?: {
    productId: string;
    name: string;
  };
}

// Project service functions
const projectService = {
  // Get all projects
  getAllProjects: async (): Promise<Project[]> => {
    const response = await api.get('/projects');
    return response.data;
  },

  // Get project by ID
  getProjectById: async (id: string): Promise<Project> => {
    const response = await api.get(`/projects/${id}`);
    return response.data;
  },

  // Create a new project
  createProject: async (project: Omit<Project, 'projectId'>): Promise<Project> => {
    const response = await api.post('/projects', project);
    return response.data;
  },

  // Update an existing project
  updateProject: async (id: string, project: Partial<Project>): Promise<Project> => {
    const response = await api.put(`/projects/${id}`, project);
    return response.data;
  },

  // Archive a project
  archiveProject: async (id: string): Promise<Project> => {
    const response = await api.put(`/projects/${id}/archive`);
    return response.data;
  },

  // Activate a project
  activateProject: async (id: string): Promise<Project> => {
    const response = await api.put(`/projects/${id}/activate`);
    return response.data;
  },

  // Get projects by product ID
  getProjectsByProductId: async (productId: string): Promise<Project[]> => {
    const response = await api.get(`/products/${productId}/projects`);
    return response.data;
  }
};

export default projectService;
