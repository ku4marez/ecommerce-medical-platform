import api from "./index";
import type {Product} from "../types/catalog";

export const catalogApi = {
  async getProduct(id: string): Promise<Product> {
    const res = await api.get(`/catalog/products/${id}`);
    return res.data;
  },

  async listProducts(params?: {
    search?: string;
    page?: number;
    size?: number;
    status?: string;
  }): Promise<{ content: Product[]; totalElements: number }> {
    const res = await api.get("/catalog/products", { params });
    return res.data;
  },

  async createProduct(product: Omit<Product, "id" | "creationDate" | "updatedDate">): Promise<Product> {
    const res = await api.post("/catalog/products", product);
    return res.data;
  },

  async updateProduct(id: string, product: Partial<Product>): Promise<Product> {
    const res = await api.put(`/catalog/products/${id}`, product);
    return res.data;
  },

  async deleteProduct(id: string): Promise<void> {
    await api.delete(`/catalog/products/${id}`);
  },
};
