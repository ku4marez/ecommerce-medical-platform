import axios from "axios";
import { storage } from "../utils/storage";

const api = axios.create({
  baseURL: import.meta.env.VITE_API_GATEWAY_URL,
  headers: { "Content-Type": "application/json" },
});

// Attach access token automatically
api.interceptors.request.use((config) => {
  const token = storage.getAccessToken();
  if (token) config.headers.Authorization = `Bearer ${token}`;
  return config;
});

api.interceptors.response.use(
  (res) => res,
  async (err) => {
    if (err.response?.status === 401 && storage.getRefreshToken()) {
      try {
        const newAccess = await refreshToken();
        err.config.headers.Authorization = `Bearer ${newAccess}`;
        return api(err.config);
      } catch {
        storage.clear();
        window.location.href = "/login";
      }
    }
    return Promise.reject(err);
  }
);

export interface AuthRequest {
  email: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  refreshToken: string;
}

// login
export async function login(req: AuthRequest): Promise<AuthResponse> {
  const { data } = await api.post<AuthResponse>("/auth/login", req);
  storage.setTokens(data.accessToken, data.refreshToken);
  return data;
}

// register
export async function register(req: AuthRequest) {
  return api.post("/auth/register", req);
}

// refresh
export async function refreshToken(): Promise<string> {
  const refreshToken = storage.getRefreshToken();
  const { data } = await api.post<AuthResponse>("/auth/refresh", { refreshToken });
  storage.setTokens(data.accessToken, data.refreshToken);
  return data.accessToken;
}

// logout
export function logout() {
  storage.clear();
}
