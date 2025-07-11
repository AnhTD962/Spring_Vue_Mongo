import axios from "axios";
import { refreshToken } from "./auth"; // Đã có trong api/auth.js

const instance = axios.create({
  baseURL: "http://localhost:8080/api",
  withCredentials: true, // cần nếu backend dùng cookie
});

// Attach access token to every request
instance.interceptors.request.use(config => {
  const token = localStorage.getItem("accessToken");
  if (token) {
    config.headers["Authorization"] = `Bearer ${token}`;
  }
  return config;
});

// Token refresh logic
let isRefreshing = false;
let failedQueue = [];

const processQueue = (error, token = null) => {
  failedQueue.forEach(prom => {
    if (error) {
      prom.reject(error);
    } else {
      prom.resolve(token);
    }
  });
  failedQueue = [];
};

instance.interceptors.response.use(
  response => response,
  async error => {
    const originalRequest = error.config;

    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (isRefreshing) {
        return new Promise((resolve, reject) => {
          failedQueue.push({ resolve, reject });
        }).then(token => {
          originalRequest.headers["Authorization"] = `Bearer ${token}`;
          return instance(originalRequest);
        });
      }

      isRefreshing = true;
      try {
        const storedRefreshToken = localStorage.getItem("refreshToken");
        const res = await refreshToken(storedRefreshToken);
        const newAccessToken = res.data.accessToken;

        localStorage.setItem("accessToken", newAccessToken);

        instance.defaults.headers.common["Authorization"] = `Bearer ${newAccessToken}`;
        originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;

        processQueue(null, newAccessToken);
        return instance(originalRequest);
      } catch (err) {
        processQueue(err, null);
        return Promise.reject(err);
      } finally {
        isRefreshing = false;
      }
    }

    return Promise.reject(error);
  }
);

export default instance;
