import api from './api';

// ScrumTeam type definitions
export interface ScrumTeam {
  id: string;
  name: string;
  businessDomain: string;
  poEmployeeId?: string;
  poName?: string;
  smEmployeeId?: string;
  smName?: string;
  productId: string;
  createdAt: string;
  updatedAt: string;
}

export interface CreateScrumTeamDto {
  productId: string;
  name: string;
  businessDomain: string;
  poEmployeeId?: string;
  poName?: string;
  smEmployeeId?: string;
  smName?: string;
}

export interface UpdateScrumTeamDto {
  name?: string;
  businessDomain?: string;
  poEmployeeId?: string;
  poName?: string;
  smEmployeeId?: string;
  smName?: string;
}

export interface AssignPoDto {
  poEmployeeId: string;
  poName: string;
}

export interface AssignSmDto {
  smEmployeeId: string;
  smName: string;
}

// ScrumTeam service functions
const scrumTeamService = {
  // Get all scrum teams for a product
  getScrumTeamsByProductId: async (productId: string): Promise<ScrumTeam[]> => {
    const response = await api.get(`/products/${productId}/scrum-teams`);
    return response.data;
  },

  // Get scrum team by ID
  getScrumTeamById: async (productId: string, scrumTeamId: string): Promise<ScrumTeam> => {
    const response = await api.get(`/products/${productId}/scrum-teams/${scrumTeamId}`);
    return response.data;
  },

  // Create a new scrum team
  createScrumTeam: async (dto: CreateScrumTeamDto): Promise<ScrumTeam> => {
    const response = await api.post(`/products/${dto.productId}/scrum-teams`, dto);
    return response.data;
  },

  // Update an existing scrum team
  updateScrumTeam: async (productId: string, scrumTeamId: string, dto: UpdateScrumTeamDto): Promise<ScrumTeam> => {
    const response = await api.patch(`/products/${productId}/scrum-teams/${scrumTeamId}`, dto);
    return response.data;
  },

  // Delete a scrum team
  deleteScrumTeam: async (productId: string, scrumTeamId: string): Promise<void> => {
    await api.delete(`/products/${productId}/scrum-teams/${scrumTeamId}`);
  },

  // Assign PO to a scrum team
  assignPo: async (productId: string, scrumTeamId: string, dto: AssignPoDto): Promise<ScrumTeam> => {
    const response = await api.patch(`/products/${productId}/scrum-teams/${scrumTeamId}/po`, dto);
    return response.data;
  },

  // Unassign PO from a scrum team
  unassignPo: async (productId: string, scrumTeamId: string): Promise<ScrumTeam> => {
    const response = await api.patch(`/products/${productId}/scrum-teams/${scrumTeamId}/po/unassign`);
    return response.data;
  },

  // Assign SM to a scrum team
  assignSm: async (productId: string, scrumTeamId: string, dto: AssignSmDto): Promise<ScrumTeam> => {
    const response = await api.patch(`/products/${productId}/scrum-teams/${scrumTeamId}/sm`, dto);
    return response.data;
  },

  // Unassign SM from a scrum team
  unassignSm: async (productId: string, scrumTeamId: string): Promise<ScrumTeam> => {
    const response = await api.patch(`/products/${productId}/scrum-teams/${scrumTeamId}/sm/unassign`);
    return response.data;
  }
};

export default scrumTeamService;
