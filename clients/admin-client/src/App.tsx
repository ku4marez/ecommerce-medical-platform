import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from "./router/ProtectedRoute";
import LoginPage from "./pages/LoginPage";

export default function App() {
  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />
        <Route element={<ProtectedRoute />}>
          <Route path="/" element={<h1>Dashboard</h1>} />
          <Route path="/orders" element={<h1>Orders</h1>} />
          <Route path="/products" element={<h1>Products</h1>} />
        </Route>
      </Routes>
    </BrowserRouter>
  );
}
