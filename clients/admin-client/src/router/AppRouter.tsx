import { createBrowserRouter, RouterProvider } from "react-router-dom";
import AppLayout from "../components/AppLayout";
import CatalogPage from "../pages/CatalogPage";
import InventoryPage from "../pages/InventoryPage";
import OrdersPage from "../pages/OrdersPage";
import PaymentsPage from "../pages/PaymentsPage";

const router = createBrowserRouter([
  {
    path: "/",
    element: <AppLayout />,
    children: [
      { path: "catalog", element: <CatalogPage /> },
      { path: "inventory", element: <InventoryPage /> },
      { path: "orders", element: <OrdersPage /> },
      { path: "payments", element: <PaymentsPage /> },
    ],
  },
]);

export default function AppRouter() {
  return <RouterProvider router={router} />;
}
