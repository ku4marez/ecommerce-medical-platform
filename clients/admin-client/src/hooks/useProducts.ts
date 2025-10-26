// src/hooks/useProducts.ts
import {useQuery} from "@tanstack/react-query";
import {catalogApi} from "../api/catalogApi";

export function useProducts() {
  return useQuery({
    queryKey: ["products"],
    queryFn: () => catalogApi.listProducts(),
  });
}
