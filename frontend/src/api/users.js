import api from "./axios";

// Admin actions
export function getUsers() {
    return api.get("/admin/users");
}

export function getUser(id) {
    return api.get(`/admin/users/${id}`);
}

export function toggleUserStatus(id, status) {
    return api.put(`/admin/users/${id}/status`, null, { params: { status } });
}

export function addAdmin(payload) {
    return api.post("/admin/add-admin", payload);
}