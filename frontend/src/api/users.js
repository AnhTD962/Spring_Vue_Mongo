import api from "./axios";

// Admin actions
export async function getUsers(page, size, type = '') {
  const response = await api.get('/user', {
    params: {
      page,
      size,
      type,
    },
  });
  return response.data;
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
