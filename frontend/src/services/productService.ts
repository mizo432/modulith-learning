import api from './api';

// Product type definition
export interface Product {
  productId: string;
  name: string;
  vision?: string;
  description?: string;
  productOwner: {
    id: string;
    name: string;
  };
}

// Product service functions
const productService = {
  // Get all products
  getAllProducts: async (): Promise<Product[]> => {
    const response = await api.get('/products');
    return response.data;
  },

  // Get product by ID
  getProductById: async (id: string): Promise<Product> => {
    const response = await api.get(`/products/${id}`);
    return response.data;
  },

  // Search products by name
  searchProductsByName: async (name: string): Promise<Product[]> => {
    const response = await api.get(`/products/search?name=${encodeURIComponent(name)}`);
    return response.data;
  }
};

export default productService;
