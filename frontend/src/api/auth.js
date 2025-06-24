import api from "./axios";

export function login(payload) {
    return api.post("/signin", payload);
}

export function register(payload) {
    return api.post("/api/register", payload);
}

export function forgotPassword(email) {
    return api.post("/auth/forgot-password", { email });
}

export function resetPassword(payload) {
    return api.post("/auth/reset-password", payload);
}

export function logout() {
    return api.post("/signout");
}

export function getProfile() {
    return api.get("/api/user/profile");
}

export function updateProfile(payload) {
    return api.put("/api/user/profile", payload);
}

export function changePassword(payload) {
    return api.put("/api/user/change-password", payload);
}