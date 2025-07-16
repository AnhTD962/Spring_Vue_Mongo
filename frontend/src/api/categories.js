import api from "./axios";

export function getCategories() {
    return api.get("/categories");
}

export function getCategory(id) {
    return api.get(`/categories/${id}`);
}

export function getCategoryActive(){
    return api.get(`/categories/all`);
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
