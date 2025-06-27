import api from "./axios";

export function getProducts(params) {
    return api.get("/admin/products", { params });
}

export function getProduct(id) {
    return api.get(`/admin/products/${id}`);
}

export function createProduct(payload) {
    return api.post("/admin/products", payload);
}

export function updateProduct(id, payload) {
    return api.put(`/admin/products/${id}`, payload);
}

export function deleteProduct(id) {
    return api.delete(`/admin/products/${id}`);
}

export function getViewProducts(){
    return api.get("/products");
}

export function getViewProductDetail(id, payload){
    return api.get(`/product/${id}`, payload);
}