import api from "./axios";

export function login(payload) {
    return api.post("/signin", payload);
}

export function register(payload) {
    return api.post("/register", payload);
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
    return api.get("/user/profile");
}

export function updateProfile(formData) {
  return api.put("/user/profile", formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
}

export function changePassword(payload) {
    return api.put("/user/change-password", payload);
}

