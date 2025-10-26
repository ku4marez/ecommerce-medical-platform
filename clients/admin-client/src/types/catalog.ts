// Types for Catalog Service
export type ProductStatus = "DRAFT" | "ACTIVE" | "ARCHIVED";

export interface Product {
  id: string;
  sku: string;
  slug: string;
  name: string;
  description?: string;
  status: ProductStatus;
  price: number;
  currency: string;
  categoryId?: string | null;
  creationDate: string;
  updatedDate: string;
}
