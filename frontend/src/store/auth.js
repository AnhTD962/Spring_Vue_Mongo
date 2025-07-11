import { defineStore } from "pinia";
import {
  login as apiLogin,
  logout as apiLogout,
  getUserProfile,
} from "../api/auth";

export const useAuthStore = defineStore("auth", {
  state: () => ({
    user: JSON.parse(localStorage.getItem("user")) || null,
    accessToken: localStorage.getItem("accessToken") || null,
    refreshToken: localStorage.getItem("refreshToken") || null,
  }),
  getters: {
    isAuthenticated: (state) => !!state.accessToken,
    userRole: (state) => state.user?.role || null,
  },
  actions: {
    async login(payload) {
      try {
        const { data } = await apiLogin(payload);

        this.accessToken = data.accessToken;
        this.refreshToken = data.refreshToken;
        this.user = {
          email: data.email,
          name: data.name,
          role: data.role,
          profileImage: data.profileImage,
        };

        // Save to localStorage
        localStorage.setItem("accessToken", data.accessToken);
        localStorage.setItem("refreshToken", data.refreshToken);
        localStorage.setItem("user", JSON.stringify(this.user));
      } catch (error) {
        throw new Error("Login failed: " + (error.response?.data?.message || error.message));
      }
    },

    async fetchProfile() {
      const { data } = await getUserProfile();
      this.user = data;
      localStorage.setItem("user", JSON.stringify(data));
    },

    async logout() {
      try {
        await apiLogout();
      } catch (_) {
        // ignore
      }
      this.user = null;
      this.accessToken = null;
      this.refreshToken = null;
      localStorage.removeItem("user");
      localStorage.removeItem("accessToken");
      localStorage.removeItem("refreshToken");
    },
  },
});
