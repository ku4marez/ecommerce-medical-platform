import { useQuery } from "@tanstack/react-query";
import { paymentApi } from "../api/paymentsApi";

export function usePayment(id: string) {
  return useQuery({
    queryKey: ["payment", id],
    queryFn: () => paymentApi.getPayment(id),
    enabled: !!id,
  });
}
