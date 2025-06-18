import api from './api';

// Team type definitions
export interface Team {
  teamId: string;
  name: string;
  description?: string;
  product?: {
    productId: string;
    name: string;
  };
}

export interface Member {
  memberId: string;
  name: string;
  email: string;
  role: string;
  teams?: Team[];
}

// Team service functions
const teamService = {
  // Get all teams
  getAllTeams: async (): Promise<Team[]> => {
    const response = await api.get('/teams');
    return response.data;
  },

  // Get team by ID
  getTeamById: async (id: string): Promise<Team> => {
    const response = await api.get(`/teams/${id}`);
    return response.data;
  },

  // Create a new team
  createTeam: async (team: Omit<Team, 'teamId'>): Promise<Team> => {
    const response = await api.post('/teams', team);
    return response.data;
  },

  // Update an existing team
  updateTeam: async (id: string, team: Partial<Team>): Promise<Team> => {
    const response = await api.put(`/teams/${id}`, team);
    return response.data;
  },

  // Delete a team
  deleteTeam: async (id: string): Promise<void> => {
    await api.delete(`/teams/${id}`);
  },

  // Get teams by product ID
  getTeamsByProductId: async (productId: string): Promise<Team[]> => {
    const response = await api.get(`/products/${productId}/teams`);
    return response.data;
  },

  // Get all members
  getAllMembers: async (): Promise<Member[]> => {
    const response = await api.get('/members');
    return response.data;
  },

  // Get member by ID
  getMemberById: async (id: string): Promise<Member> => {
    const response = await api.get(`/members/${id}`);
    return response.data;
  },

  // Create a new member
  createMember: async (member: Omit<Member, 'memberId'>): Promise<Member> => {
    const response = await api.post('/members', member);
    return response.data;
  },

  // Update an existing member
  updateMember: async (id: string, member: Partial<Member>): Promise<Member> => {
    const response = await api.put(`/members/${id}`, member);
    return response.data;
  },

  // Delete a member
  deleteMember: async (id: string): Promise<void> => {
    await api.delete(`/members/${id}`);
  },

  // Get members of a team
  getTeamMembers: async (teamId: string): Promise<Member[]> => {
    const response = await api.get(`/teams/${teamId}/members`);
    return response.data;
  },

  // Add a member to a team
  addMemberToTeam: async (teamId: string, memberId: string): Promise<void> => {
    await api.post(`/teams/${teamId}/members/${memberId}`);
  },

  // Remove a member from a team
  removeMemberFromTeam: async (teamId: string, memberId: string): Promise<void> => {
    await api.delete(`/teams/${teamId}/members/${memberId}`);
  }
};

export default teamService;
