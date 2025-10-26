// Types for Payment Service
export type PaymentStatus = "PENDING" | "SUCCEEDED" | "FAILED";
export type RefundStatus = "PENDING" | "COMPLETED" | "FAILED";

export interface Payment {
  id: string;
  orderId: string;
  provider: "STRIPE" | "OTHER";
  status: PaymentStatus;
  checkoutUrl?: string;
  providerRef?: string;
  creationDate: string;
  updatedDate: string;
}

export interface Refund {
  id: string;
  paymentId: string;
  amount: number;
  currency: string;
  status: RefundStatus;
  reason?: string;
  creationDate: string;
  updatedDate: string;
}
