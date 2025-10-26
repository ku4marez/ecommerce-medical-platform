import api from "./index";
import type {StockItem, Reservation} from "../types/inventory";

export const inventoryApi = {
  async getStock(productId: string): Promise<StockItem> {
    const res = await api.get(`/inventory/stock/${productId}`);
    return res.data;
  },

  async getReservation(productId: string, orderId: string): Promise<Reservation> {
    const res = await api.get(`/inventory/reservations/${productId}/${orderId}`);
    return res.data;
  },

  async reserve(req: {
    productId: string;
    orderId: string;
    quantity: number;
    ttlSeconds?: number;
    idempotencyKey?: string;
  }): Promise<Reservation> {
    const res = await api.post("/inventory/reserve", req);
    return res.data;
  },

  async release(req: { productId: string; orderId: string; reason?: string }): Promise<Reservation> {
    const res = await api.post("/inventory/release", req);
    return res.data;
  },

  async confirm(req: { productId: string; orderId: string }): Promise<Reservation> {
    const res = await api.post("/inventory/confirm", req);
    return res.data;
  },

  async adjust(req: { productId: string; delta: number; reason?: string }): Promise<StockItem> {
    const res = await api.post("/inventory/adjust", req);
    return res.data;
  },
};
