import api from "./index";
import type {Order} from "../types/orders";

export const orderApi = {
  async getOrder(id: string): Promise<Order> {
    const res = await api.get(`/orders/${id}`);
    return res.data;
  },

  async listOrders(params: { customerId?: string; page?: number; size?: number }): Promise<{
    content: Order[];
    totalElements: number;
  }> {
    const res = await api.get("/orders", { params });
    return res.data;
  },

  async createOrder(req: {
    customerId: string;
    items: { productId: string; quantity: number; unitPrice: number }[];
  }): Promise<Order> {
    const res = await api.post("/orders", req);
    return res.data;
  },

  async updateStatus(id: string, status: string): Promise<Order> {
    const res = await api.patch(`/orders/${id}/status`, null, { params: { status } });
    return res.data;
  },
};
