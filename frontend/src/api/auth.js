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

