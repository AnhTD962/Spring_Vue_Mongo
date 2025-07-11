import api from "./axios";

export function login(payload) {
    return api.post("/signin", payload);
}

export function register(payload) {
    return api.post("/register", payload);
}

export function forgotPassword(email) {
    return api.post("/forgot-password", { email });
}

export function logout() {
    return api.post("/signout");
}

export function refreshToken(refreshToken) {
  return api.post("/refresh-token", { refreshToken });
}

export function getUserProfile() {
    return api.get("/user/profile");
}

export function getAdminProfile() {
    return api.get("/admin/profile");
}

export function updateUserProfile(formData) {
  return api.put("/user/profile", formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
}

export function updateAdminProfile(formData) {
  return api.put("/admin/profile", formData, {
    headers: {
      "Content-Type": "multipart/form-data"
    }
  });
}

export function changeUserPassword(payload) {
    return api.put("/user/change-password", payload);
}

export function changeAdminPassword(payload) {
    return api.put("/admin/change-password", payload);
}

