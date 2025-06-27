import { defineStore } from "pinia";
import { login as apiLogin, logout as apiLogout, getUserProfile } from "../api/auth";

export const useAuthStore = defineStore("auth", {
    state: () => ({
        user: JSON.parse(localStorage.getItem("user")) || null,
    }),
    getters: {
        isAuthenticated: state => !!state.user,
        userRole: state => state.user?.role || null
    },
    actions: {
        async login(payload) {
            const { data } = await apiLogin(payload);
            if (data.success) {
                this.user = data.user; 
                localStorage.setItem("user", JSON.stringify(data.user));
            } else {
                throw new Error(data.message || "Login failed");
            }
        },
        async fetchProfile() {
            const { data } = await getUserProfile();
            this.user = data;
            localStorage.setItem("user", JSON.stringify(data));
        },
        async logout() {
            await apiLogout();
            this.user = null;
            localStorage.removeItem("user");
        }
    }
});