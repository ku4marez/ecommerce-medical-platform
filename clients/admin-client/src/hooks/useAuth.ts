import { useState } from "react";
import { login as loginApi, logout as logoutApi, type AuthRequest } from "../api/authApi";
import { storage } from "../utils/storage";

export function useAuth() {
  const [isAuthenticated, setAuthenticated] = useState(!!storage.getAccessToken());

  async function login(req: AuthRequest) {
    await loginApi(req);
    setAuthenticated(true);
  }

  function logout() {
    logoutApi();
    setAuthenticated(false);
  }

  return { isAuthenticated, login, logout };
}
