import { useQuery } from "@tanstack/react-query";
import { orderApi } from "../api/ordersApi";

export function useOrders(customerId?: string) {
  return useQuery({
    queryKey: ["orders", customerId],
    queryFn: () => orderApi.listOrders({ customerId }),
  });
}
