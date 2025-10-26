// Types for Inventory Service
export type ReservationStatus = "PENDING" | "CONFIRMED" | "RELEASED" | "EXPIRED";

export interface StockItem {
  id: string;
  productId: string;
  available: number;
  reserved: number;
  creationDate: string;
  updatedDate: string;
}

export interface Reservation {
  id: string;
  productId: string;
  orderId: string;
  quantity: number;
  status: ReservationStatus;
  expiresAt: string;
  creationDate: string;
  updatedDate: string;
}
