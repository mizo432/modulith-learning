import api from './api';

// Product status type
export type ProductStatus = 'ACTIVE' | 'ARCHIVED';

// Product type definition
export interface Product {
  id: number;
  name: string;
  description?: string;
  managerId?: string;
  status: ProductStatus;
  createdAt: string;
  updatedAt: string;
}

// Product create request type
export interface ProductCreateRequest {
  name: string;
  description?: string;
  managerId?: string;
}

// Product update request type
export interface ProductUpdateRequest {
  name?: string;
  description?: string;
  managerId?: string;
}

// Product service functions
const productService = {
  // Get all products
  getAllProducts: async (): Promise<Product[]> => {
    const response = await api.get('/v1/products');
    return response.data;
  },

  // Get product by ID
  getProductById: async (id: number): Promise<Product> => {
    const response = await api.get(`/v1/products/${id}`);
    return response.data;
  },

  // Create a new product
  createProduct: async (product: ProductCreateRequest): Promise<Product> => {
    const response = await api.post('/v1/products', product);
    return response.data;
  },

  // Update an existing product
  updateProduct: async (id: number, product: ProductUpdateRequest): Promise<Product> => {
    const response = await api.patch(`/v1/products/${id}`, product);
    return response.data;
  },

  // Archive a product
  archiveProduct: async (id: number): Promise<Product> => {
    const response = await api.patch(`/v1/products/${id}/archive`);
    return response.data;
  },

  // Delete a product (logical delete)
  deleteProduct: async (id: number): Promise<void> => {
    await api.delete(`/v1/products/${id}`);
  },

  // Search products by name
  searchProductsByName: async (name: string): Promise<Product[]> => {
    const response = await api.get(`/v1/products/search?name=${encodeURIComponent(name)}`);
    return response.data;
  }
};

export default productService;
