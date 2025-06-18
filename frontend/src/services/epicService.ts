import api from './api';

// Epic type definitions
export interface Epic {
  epicId: string;
  title: string;
  description?: string;
  status: string;
  priority?: string;
  startDate?: string; // ISO date string
  endDate?: string; // ISO date string
  product: {
    productId: string;
    name: string;
  };
}

export interface UserStory {
  storyId: string;
  title: string;
  description?: string;
  status: string;
  priority?: string;
  points?: number;
  epic?: {
    epicId: string;
    title: string;
  };
}

// Epic service functions
const epicService = {
  // Get all epics
  getAllEpics: async (): Promise<Epic[]> => {
    const response = await api.get('/epics');
    return response.data;
  },

  // Get epic by ID
  getEpicById: async (id: string): Promise<Epic> => {
    const response = await api.get(`/epics/${id}`);
    return response.data;
  },

  // Create a new epic
  createEpic: async (epic: Omit<Epic, 'epicId'>): Promise<Epic> => {
    const response = await api.post('/epics', epic);
    return response.data;
  },

  // Update an existing epic
  updateEpic: async (id: string, epic: Partial<Epic>): Promise<Epic> => {
    const response = await api.put(`/epics/${id}`, epic);
    return response.data;
  },

  // Delete an epic
  deleteEpic: async (id: string): Promise<void> => {
    await api.delete(`/epics/${id}`);
  },

  // Get epics by product ID
  getEpicsByProductId: async (productId: string): Promise<Epic[]> => {
    const response = await api.get(`/products/${productId}/epics`);
    return response.data;
  },

  // Get user stories for an epic
  getUserStoriesForEpic: async (epicId: string): Promise<UserStory[]> => {
    const response = await api.get(`/epics/${epicId}/user-stories`);
    return response.data;
  },

  // Associate a user story with an epic
  associateUserStoryWithEpic: async (epicId: string, storyId: string): Promise<UserStory> => {
    const response = await api.put(`/epics/${epicId}/user-stories/${storyId}`);
    return response.data;
  },

  // Disassociate a user story from an epic
  disassociateUserStoryFromEpic: async (storyId: string): Promise<UserStory> => {
    const response = await api.delete(`/user-stories/${storyId}/epic`);
    return response.data;
  },

  // Get all user stories
  getAllUserStories: async (): Promise<UserStory[]> => {
    const response = await api.get('/user-stories');
    return response.data;
  },

  // Get user story by ID
  getUserStoryById: async (id: string): Promise<UserStory> => {
    const response = await api.get(`/user-stories/${id}`);
    return response.data;
  },

  // Create a new user story
  createUserStory: async (userStory: Omit<UserStory, 'storyId'>): Promise<UserStory> => {
    const response = await api.post('/user-stories', userStory);
    return response.data;
  },

  // Update an existing user story
  updateUserStory: async (id: string, userStory: Partial<UserStory>): Promise<UserStory> => {
    const response = await api.put(`/user-stories/${id}`, userStory);
    return response.data;
  },

  // Delete a user story
  deleteUserStory: async (id: string): Promise<void> => {
    await api.delete(`/user-stories/${id}`);
  }
};

export default epicService;
