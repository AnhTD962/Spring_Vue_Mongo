import api from "./axios";

export function getCategories(page = 0, size = 6) {
    return api.get(`/categories?page=${page}&size=${size}`);
}

export function getCategory(id) {
    return api.get(`/categories/${id}`);
}

export function getCategoryActive(page = 0, size = 6) {
    return api.get(`/categories/all?page=${page}&size=${size}`);
}

export function createCategory(payload) {
    return api.post("/categories", payload);
}

export function updateCategory(id, payload) {
    return api.put(`/categories/${id}`, payload);
}

export function deleteCategory(id) {
    return api.delete(`/categories/${id}`);
}
