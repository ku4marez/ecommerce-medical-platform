// Types for Order Service
export type OrderStatus =
  | "NEW"
  | "RESERVED"
  | "CONFIRMED"
  | "PAID"
  | "CANCELLED";

export interface OrderItem {
  productId: string;
  quantity: number;
  unitPrice: number;
}

export interface Order {
  id: string;
  customerId: string;
  status: OrderStatus;
  totalAmount: number;
  items: OrderItem[];
  creationDate: string;
  updatedDate: string;
}
