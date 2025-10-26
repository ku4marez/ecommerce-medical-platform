import { useQuery } from "@tanstack/react-query";
import { inventoryApi } from "../api/inventoryApi";

export function useStock(productId: string) {
  return useQuery({
    queryKey: ["stock", productId],
    queryFn: () => inventoryApi.getStock(productId),
    enabled: !!productId,
  });
}
