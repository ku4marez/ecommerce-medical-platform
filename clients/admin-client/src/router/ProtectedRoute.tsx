import { Navigate, Outlet } from "react-router-dom";
import { storage } from "../utils/storage";

export default function ProtectedRoute() {
  const isAuth = !!storage.getAccessToken();
  return isAuth ? <Outlet /> : <Navigate to="/login" replace />;
}
