import api from "./index.js";
import type {Payment, Refund} from "../types/payments";

export const paymentApi = {
  async getPayment(id: string): Promise<Payment> {
    const res = await api.get(`/payments/${id}`);
    return res.data;
  },

  async createPayment(req: { orderId: string }): Promise<Payment> {
    const res = await api.post("/payments", req);
    return res.data;
  },

  async refund(req: { paymentId: string; amount: number; reason?: string }): Promise<Refund> {
    const res = await api.post("/payments/refunds", req);
    return res.data;
  },
};
