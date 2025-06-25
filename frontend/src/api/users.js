import api from "./axios";

// Admin actions
export function getUsers() {
    return api.get("/api/admin/users");
}

export function getUser(id) {
    return api.get(`/api/admin/users/${id}`);
}

export function toggleUserStatus(id, status) {
    return api.put(`/api/admin/users/${id}/status`, null, { params: { status } });
}

export function addAdmin(payload) {
    return api.post("/api/admin/admins", payload);
}