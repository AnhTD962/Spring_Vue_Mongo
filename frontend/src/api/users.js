import api from "./axios";

// Admin actions
export function getUsers(type = "") {
  return api.get("/user", {
    params: type ? { type } : {},
  });
}

export function getUser(id) {
  return api.get(`/user/${id}`);
}

export function toggleUserStatus(id, status) {
  return api.put(`/user/${id}/status`, null, {
    params: { status },
  });
}

export function addAdmin(payload) {
  return api.post("/user/add-admin", payload);
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
